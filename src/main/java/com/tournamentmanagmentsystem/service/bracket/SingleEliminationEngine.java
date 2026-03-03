package com.tournamentmanagmentsystem.service.bracket;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
     * Generates the first round of matches.
     * Pairs participants randomly and handles "byes" for odd numbers.
     *
     * @param eventId      the event UUID
     * @param participants the list of registered participants
     * @return persisted list of initial matches
     */
    @Override
    @NonNull
    public List<Match> generateInitialMatches(@NonNull UUID eventId, @NonNull List<Participant> participants) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        List<Participant> seeds = prepareSeeds(participants);
        List<Match> initialMatches = createFirstRound(event, seeds);

        return matchRepository.saveAll(initialMatches);
    }

    /**
     * Logic to move a winner to a subsequent match in the bracket.
     * Currently a placeholder for advanced bracket tree management.
     *
     * @param finishedMatch the match that just concluded
     * @return the next match the winner is assigned to, or null if none
     */
    @Override
    @Nullable
    public Match advanceWinner(@NonNull Match finishedMatch) {
        // TODO: Implement advanced tree traversal to find the next placeholder match
        return null;
    }

    private List<Participant> prepareSeeds(List<Participant> participants) {
        List<Participant> seeds = new ArrayList<>(participants);
        Collections.shuffle(seeds); // Default to random seeding
        return seeds;
    }

    private List<Match> createFirstRound(Event event, List<Participant> seeds) {
        List<Match> matches = new ArrayList<>();
        int numParticipants = seeds.size();

        // Create pairs
        for (int i = 0; i < numParticipants - 1; i += 2) {
            matches.add(buildMatch(event, seeds.get(i), seeds.get(i + 1)));
        }

        // Handle bye if participants are odd
        if (numParticipants % 2 != 0) {
            matches.add(buildByeMatch(event, seeds.get(numParticipants - 1)));
        }

        return matches;
    }

    private Match buildMatch(Event event, Participant pA, Participant pB) {
        return Match.builder()
                .event(event)
                .stage("ROUND_1")
                .roundNumber(1)
                .participantA(pA)
                .participantB(pB)
                .status(MatchStatus.PENDING)
                .build();
    }

    private Match buildByeMatch(Event event, Participant pA) {
        return Match.builder()
                .event(event)
                .stage("ROUND_1")
                .roundNumber(1)
                .participantA(pA)
                .status(MatchStatus.COMPLETED)
                .winnerId(pA.getId())
                .score(Collections.singletonMap("note", "BYE"))
                .build();
    }
}
