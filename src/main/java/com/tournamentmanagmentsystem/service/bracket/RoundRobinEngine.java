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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of a Round Robin (League) tournament structure.
 * Every participant plays against every other participant.
 */
@Service
@RequiredArgsConstructor
public class RoundRobinEngine implements BracketEngine {

    private final MatchRepository matchRepository;
    private final EventRepository eventRepository;

    /**
     * Generates a complete set of matches where everyone faces everyone once.
     *
     * @param eventId      the event UUID
     * @param participants the pool of players/teams
     * @return all scheduled league matches
     */
    @Override
    @NonNull
    public List<Match> generateInitialMatches(@NonNull UUID eventId, @NonNull List<Participant> participants) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));

        List<Match> leagueMatches = generateAllPermutations(Objects.requireNonNull(event, "Event must not be null"),
                participants);

        return Objects.requireNonNull(matchRepository.saveAll(leagueMatches), "Saved matches must not be null");
    }

    /**
     * Round Robin does not typically have an "advance" logic as it's point-based.
     */
    @Override
    @Nullable
    public Match advanceWinner(@NonNull Match finishedMatch) {
        return null;
    }

    @NonNull
    private List<Match> generateAllPermutations(@NonNull Event event, @NonNull List<Participant> participants) {
        List<Match> matches = new ArrayList<>();
        int n = participants.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                matches.add(createLeagueMatch(event,
                        Objects.requireNonNull(participants.get(i), "Participant A must not be null"),
                        Objects.requireNonNull(participants.get(j), "Participant B must not be null")));
            }
        }
        return matches;
    }

    @NonNull
    private Match createLeagueMatch(@NonNull Event event, @NonNull Participant p1, @NonNull Participant p2) {
        Match match = Match.builder()
                .event(event)
                .stage("LEAGUE")
                .participantA(p1)
                .participantB(p2)
                .status(MatchStatus.PENDING)
                .build();
        return Objects.requireNonNull(match, "Match must not be null");
    }
}
