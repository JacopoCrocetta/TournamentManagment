package com.tournamentmanagmentsystem.service.bracket;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;
import com.tournamentmanagmentsystem.domain.enums.MatchStatus;
import com.tournamentmanagmentsystem.exception.ResourceNotFoundException;
import com.tournamentmanagmentsystem.repository.EventRepository;
import com.tournamentmanagmentsystem.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));

        List<Participant> seeds = prepareSeeds(participants);
        List<Match> initialMatches = createFirstRound(Objects.requireNonNull(event, "Event must not be null"), seeds);

        return Objects.requireNonNull(matchRepository.saveAll(initialMatches), "Saved matches must not be null");
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

    @NonNull
    private List<Participant> prepareSeeds(@NonNull List<Participant> participants) {
        List<Participant> seeds = new ArrayList<>(participants);
        Collections.shuffle(seeds); // Default to random seeding
        return seeds;
    }

    @NonNull
    private List<Match> createFirstRound(@NonNull Event event, @NonNull List<Participant> seeds) {
        List<Match> matches = new ArrayList<>();
        int numParticipants = seeds.size();

        // Create pairs
        for (int i = 0; i < numParticipants - 1; i += 2) {
            matches.add(buildMatch(event,
                    Objects.requireNonNull(seeds.get(i), "Participant A must not be null"),
                    Objects.requireNonNull(seeds.get(i + 1), "Participant B must not be null")));
        }

        // Handle bye if participants are odd
        if (numParticipants % 2 != 0) {
            matches.add(buildByeMatch(event,
                    Objects.requireNonNull(seeds.get(numParticipants - 1), "Bye participant must not be null")));
        }

        return matches;
    }

    @NonNull
    private Match buildMatch(@NonNull Event event, @NonNull Participant pA, @NonNull Participant pB) {
        Match match = Match.builder()
                .event(event)
                .stage("ROUND_1")
                .roundNumber(1)
                .participantA(pA)
                .participantB(pB)
                .status(MatchStatus.PENDING)
                .build();
        return Objects.requireNonNull(match, "Match must not be null");
    }

    @NonNull
    private Match buildByeMatch(@NonNull Event event, @NonNull Participant pA) {
        Match match = Match.builder()
                .event(event)
                .stage("ROUND_1")
                .roundNumber(1)
                .participantA(pA)
                .status(MatchStatus.COMPLETED)
                .winnerId(pA.getId())
                .score(Collections.singletonMap("note", "BYE"))
                .build();
        return Objects.requireNonNull(match, "Bye match must not be null");
    }
}
