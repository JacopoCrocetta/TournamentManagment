package com.tournamentmanagmentsystem.service.bracket;


import org.springframework.lang.Nullable;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.domain.enums.SeedingPolicy;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.tournamentmanagmentsystem.exception.NotFoundException;

/**
 * Implementation of a Single Elimination tournament structure.
 * Losers are immediately eliminated, and winners progress to the next round.
 */
@Service
@RequiredArgsConstructor
public class SingleEliminationEngine implements BracketEngine {

    private final MatchRepository matchRepository;
    private final EventRepository eventRepository;

    /**
     * Generates the entire bracket, creating placeholder matches for future rounds.
     * Starts by pairing participants and handling "byes" for numbers not power of 2.
     *
     * @param eventId      the event UUID
     * @param participants the list of registered participants
     * @return persisted list of ALL matches in the bracket
     */
    @Override
    public List<Match> generateInitialMatches(UUID eventId, List<Participant> participants) {
        if (participants.isEmpty()) {
            return Collections.emptyList();
        }
        
        Event event = eventRepository.findById(Objects.requireNonNull(eventId))
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        List<Participant> sortedParticipants = prepareSeeds(participants, event.getSeedingPolicy());
        
        int n = 1;
        while (n < sortedParticipants.size()) {
            n *= 2;
        }
        
        List<Participant> bracketSeeds = arrangeBracket(sortedParticipants, n);

        int numRounds = (int) (Math.log(n) / Math.log(2));
        List<Match> allMatches = new ArrayList<>();

        // Generate Round 1 matches
        for (int i = 0; i < n / 2; i++) {
            Match m = buildMatch(event, 1, i, bracketSeeds.get(2 * i), bracketSeeds.get(2 * i + 1));
            // Auto-advance if BYE (one participant is null)
            if (m.getParticipantA() == null || m.getParticipantB() == null) {
                m.setStatus(MatchStatus.COMPLETED);
                Participant winner = m.getParticipantA() != null ? m.getParticipantA() : m.getParticipantB();
                if (winner != null) {
                    m.setWinnerId(winner.getId());
                    m.setScore(Collections.singletonMap("note", "BYE"));
                }
            }
            allMatches.add(m);
        }

        // Generate subsequent rounds (placeholder matches)
        for (int r = 2; r <= numRounds; r++) {
            int roundMatchCount = n / (int) Math.pow(2, r);
            for (int i = 0; i < roundMatchCount; i++) {
                allMatches.add(buildMatch(event, r, i, null, null));
            }
        }

        List<Match> savedMatches = matchRepository.saveAll(allMatches);

        // Auto-advance winners from bye matches.
        // Needs a separate loop because subsequent rounds must exist in DB before advanceWinner queries them.
        for (Match m : savedMatches) {
            if (m.getStatus() == MatchStatus.COMPLETED && m.getRoundNumber() == 1) {
                advanceWinner(m);
            }
        }

        return savedMatches;
    }

    /**
     * Logic to move a winner to a subsequent match in the bracket.
     * Exploits the stage string "ROUND_INDEX" to determine next node in tree.
     *
     * @param finishedMatch the match that just concluded
     * @return the next match the winner is assigned to, or null if none
     */
    @Override
    @Nullable
    public Match advanceWinner(Match finishedMatch) {
        if (finishedMatch.getWinnerId() == null) {
            return null;
        }

        Participant winnerParticipant = null;
        if (finishedMatch.getParticipantA() != null && finishedMatch.getWinnerId().equals(finishedMatch.getParticipantA().getId())) {
            winnerParticipant = finishedMatch.getParticipantA();
        } else if (finishedMatch.getParticipantB() != null && finishedMatch.getWinnerId().equals(finishedMatch.getParticipantB().getId())) {
            winnerParticipant = finishedMatch.getParticipantB();
        }

        if (winnerParticipant == null) {
            return null; // Could not resolve winner participant
        }

        String currentStage = finishedMatch.getStage();
        if (currentStage == null || !currentStage.contains("_")) return null;

        String[] parts = currentStage.split("_");
        int currentRound = Integer.parseInt(parts[0]);
        int currentMatchIndex = Integer.parseInt(parts[1]);

        int nextRound = currentRound + 1;
        int nextMatchIndex = currentMatchIndex / 2;
        String nextStage = nextRound + "_" + nextMatchIndex;

        Participant finalWinnerParticipant = winnerParticipant;
        return matchRepository.findByEventIdAndStage(finishedMatch.getEvent().getId(), nextStage)
                .map(nextMatch -> {
                    if (currentMatchIndex % 2 == 0) {
                        nextMatch.setParticipantA(finalWinnerParticipant);
                    } else {
                        nextMatch.setParticipantB(finalWinnerParticipant);
                    }
                    return matchRepository.save(nextMatch);
                })
                .orElse(null);
    }

    private List<Participant> prepareSeeds(List<Participant> participants, SeedingPolicy policy) {
        List<Participant> seeds = new ArrayList<>(participants);
        if (policy == null) policy = SeedingPolicy.RANDOM;

        switch (policy) {
            case MANUAL:
                seeds.sort((p1, p2) -> {
                    Integer s1 = p1.getSeed() != null ? p1.getSeed() : Integer.MAX_VALUE;
                    Integer s2 = p2.getSeed() != null ? p2.getSeed() : Integer.MAX_VALUE;
                    return Integer.compare(s1, s2);
                });
                break;
            case RATING:
                seeds.sort((p1, p2) -> {
                    Integer r1 = p1.getRating() != null ? p1.getRating() : Integer.MIN_VALUE;
                    Integer r2 = p2.getRating() != null ? p2.getRating() : Integer.MIN_VALUE;
                    return Integer.compare(r2, r1); // Descending highest first
                });
                break;
            case RANDOM:
            default:
                Collections.shuffle(seeds);
                break;
        }
        return seeds;
    }

    private List<Participant> arrangeBracket(List<Participant> sortedSeeds, int powerOfTwo) {
        List<Participant> bracket = new ArrayList<>(Collections.nCopies(powerOfTwo, (Participant) null));

        List<Integer> positions = new ArrayList<>();
        positions.add(0);

        int currentSize = 1;
        while (currentSize < powerOfTwo) {
            List<Integer> nextPositions = new ArrayList<>();
            for (int pos : positions) {
                nextPositions.add(pos);
                nextPositions.add(2 * currentSize - 1 - pos);
            }
            positions = nextPositions;
            currentSize *= 2;
        }

        for (int i = 0; i < sortedSeeds.size(); i++) {
            int bracketPos = positions.get(i);
            bracket.set(bracketPos, sortedSeeds.get(i));
        }
        return bracket;
    }

    private Match buildMatch(Event event, int roundNumber, int matchIndex, Participant pA, Participant pB) {
        return Match.builder()
                .event(event)
                .stage(roundNumber + "_" + matchIndex)
                .roundNumber(roundNumber)
                .participantA(pA)
                .participantB(pB)
                .status(MatchStatus.PENDING)
                .build();
    }
}
