package com.tournamentmanagmentsystem.service.bracket;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.domain.enums.SeedingPolicy;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.tournamentmanagmentsystem.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class DoubleEliminationEngine implements BracketEngine {

    private final MatchRepository matchRepository;
    private final EventRepository eventRepository;

    @Override
    @NonNull
    public List<Match> generateInitialMatches(@NonNull UUID eventId, @NonNull List<Participant> participants) {
        if (participants.isEmpty()) {
            return Collections.emptyList();
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        List<Participant> sortedParticipants = prepareSeeds(participants, event.getSeedingPolicy());

        int n = 1;
        while (n < sortedParticipants.size()) {
            n *= 2;
        }

        List<Participant> bracketSeeds = arrangeBracket(sortedParticipants, n);
        int numWbRounds = (int) (Math.log(n) / Math.log(2));

        List<Match> allMatches = new ArrayList<>();

        // 1. Generate Winner Bracket (WB)
        for (int r = 1; r <= numWbRounds; r++) {
            int roundMatchCount = n / (int) Math.pow(2, r);
            for (int i = 0; i < roundMatchCount; i++) {
                Match m = buildMatch(event, "W_" + r + "_" + i, r);
                if (r == 1) {
                    m.setParticipantA(bracketSeeds.get(2 * i));
                    m.setParticipantB(bracketSeeds.get(2 * i + 1));
                    handleByes(m);
                }
                allMatches.add(m);
            }
        }

        // 2. Generate Loser Bracket (LB)
        int numLbRounds = 2 * numWbRounds - 2;
        int lbMatchesInRound = n / 4;
        for (int r = 1; r <= numLbRounds; r++) {
            for (int i = 0; i < lbMatchesInRound; i++) {
                allMatches.add(buildMatch(event, "L_" + r + "_" + i, r));
            }
            if (r % 2 == 0) {
                lbMatchesInRound /= 2;
            }
        }

        // 3. Generate Grand Final
        allMatches.add(buildMatch(event, "GF_1_0", 1));
        
        // Optional second GF match
        allMatches.add(buildMatch(event, "GF_2_0", 2));

        List<Match> savedMatches = matchRepository.saveAll(allMatches);

        // Auto-advance byes in WB round 1
        for (Match m : savedMatches) {
            // Simplified handling: byes will advance via typical resolve flow if needed,
            // or we directly advance them. Let's just directly advance winners of complected round 1 matches.
            if (m.getStatus() == MatchStatus.COMPLETED && "W_1".equals(m.getStage().substring(0, 3))) {
                advanceWinner(m);
            }
        }

        return savedMatches;
    }

    @Override
    @Nullable
    public Match advanceWinner(@NonNull Match finishedMatch) {
        if (finishedMatch.getWinnerId() == null) return null;

        Participant winner = getParticipantById(finishedMatch, finishedMatch.getWinnerId());
        Participant loser = getParticipantById(finishedMatch, getLoserId(finishedMatch));

        if (winner == null || loser == null) return null;

        String stage = finishedMatch.getStage();
        if (stage == null) return null;

        String[] parts = stage.split("_");
        if (parts.length < 3) return null;

        String bracket = parts[0];
        int r = Integer.parseInt(parts[1]);
        int idx = Integer.parseInt(parts[2]);

        UUID eventId = finishedMatch.getEvent().getId();

        if ("W".equals(bracket)) {
            // Winner goes to next W round, Loser goes to LB
            // Winner advancement
            int nextRoundMatchCount = getWBMatchCount(r + 1, eventId);
            if (nextRoundMatchCount > 0) {
                int nextR = r + 1;
                int nextIdx = idx / 2;
                advanceParticipant(eventId, "W_" + nextR + "_" + nextIdx, idx % 2, winner);
            } else {
                // If WB Final is completed, advance to GF
                advanceParticipant(eventId, "GF_1_0", 0, winner);
            }

            // Loser advancement
            if (r == 1) {
                // WB R1 losers drop to LB R1
                int nextIdx = idx / 2;
                advanceParticipant(eventId, "L_1_" + nextIdx, idx % 2, loser);
            } else {
                // WB R(r) losers drop to LB R(2r - 2)
                int nextLbR = 2 * r - 2;
                // Place loser in position 1 (participant B) of the LB match
                advanceParticipant(eventId, "L_" + nextLbR + "_" + idx, 1, loser);
            }
        } else if ("L".equals(bracket)) {
            // Winner goes to next LB round. Loser is eliminated forever.
            int nextR = r + 1;
            if (hasMatch(eventId, "L_" + nextR + "_0")) {
                if (nextR % 2 != 0) {
                    // Next round matches players strictly from LB
                    int nextIdx = idx / 2;
                    advanceParticipant(eventId, "L_" + nextR + "_" + nextIdx, idx % 2, winner);
                } else {
                    // Next round receives players from WB, so WB loser taking participant B
                    advanceParticipant(eventId, "L_" + nextR + "_" + idx, 0, winner);
                }
            } else {
                // LB final winner goes to GF
                advanceParticipant(eventId, "GF_1_0", 1, winner);
            }
        } else if ("GF".equals(bracket)) {
            // If GF_1_0 and LB player wins, trigger GF_2_0
            if (r == 1) {
                // If participant B (loser bracket winner) wins
                if (winner.getId().equals(finishedMatch.getParticipantB().getId())) {
                    advanceParticipant(eventId, "GF_2_0", 0, finishedMatch.getParticipantA());
                    advanceParticipant(eventId, "GF_2_0", 1, finishedMatch.getParticipantB());
                }
            }
        }

        return null;
    }

    private Participant getParticipantById(Match match, UUID id) {
        if (id == null) return null;
        if (match.getParticipantA() != null && id.equals(match.getParticipantA().getId())) return match.getParticipantA();
        if (match.getParticipantB() != null && id.equals(match.getParticipantB().getId())) return match.getParticipantB();
        return null; // For bye
    }

    private UUID getLoserId(Match match) {
        if (match.getWinnerId() == null) return null;
        if (match.getParticipantA() != null && match.getWinnerId().equals(match.getParticipantA().getId())) {
            return match.getParticipantB() != null ? match.getParticipantB().getId() : null;
        }
        return match.getParticipantA() != null ? match.getParticipantA().getId() : null;
    }

    private void advanceParticipant(UUID eventId, String stage, int participantIndex, Participant participant) {
        matchRepository.findByEventIdAndStage(eventId, stage).ifPresent(nextMatch -> {
            if (participantIndex == 0) {
                nextMatch.setParticipantA(participant);
            } else {
                nextMatch.setParticipantB(participant);
            }
            matchRepository.save(nextMatch);
            
            // if it becomes a BYE inherently (one null dropping in) we might auto-resolve, 
            // but usually in Double Elim, we just wait. We can auto-resolve BYEs similarly.
            if (nextMatch.getParticipantA() != null && nextMatch.getParticipantB() == null && "BYE".equals(nextMatch.getParticipantA().getName())) {
               // simplified logic skipping here.
            }
        });
    }

    private Match buildMatch(Event event, String stage, int round) {
        return Match.builder()
                .event(event)
                .stage(stage)
                .roundNumber(round)
                .status(MatchStatus.PENDING)
                .build();
    }
    
    private void handleByes(Match m) {
        if (m.getParticipantA() == null || m.getParticipantB() == null) {
            m.setStatus(MatchStatus.COMPLETED);
            Participant winner = m.getParticipantA() != null ? m.getParticipantA() : m.getParticipantB();
            if (winner != null) {
                m.setWinnerId(winner.getId());
                m.setScore(Collections.singletonMap("note", "BYE"));
            }
        }
    }

    private boolean hasMatch(UUID eventId, String stage) {
        return matchRepository.findByEventIdAndStage(eventId, stage).isPresent();
    }

    // Simplified checks
    private int getWBMatchCount(int r, UUID eventId) {
        return matchRepository.findByEventIdAndStage(eventId, "W_" + r + "_0").isPresent() ? 1 : 0; // naive check if round exists
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
                    return Integer.compare(r2, r1);
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
            bracket.set(positions.get(i), sortedSeeds.get(i));
        }
        return bracket;
    }
}
