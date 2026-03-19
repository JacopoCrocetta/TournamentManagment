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
    public List<Match> generateInitialMatches(UUID eventId, List<Participant> participants) {
        Event event = eventRepository.findById(Objects.requireNonNull(eventId))
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        List<Participant> sorted = prepareSeeds(participants, event.getSeedingPolicy());
        List<Match> leagueMatches = generateAllPermutations(event, sorted);

        Iterable<Match> savedMatches = matchRepository.saveAll(Objects.requireNonNull(leagueMatches));
        List<Match> result = new ArrayList<>();
        savedMatches.forEach(result::add);
        return result;
    }

    /**
     * Round Robin does not typically have an "advance" logic as it's point-based.
     */
    @Override
    @Nullable
    public Match advanceWinner(Match finishedMatch) {
        return null;
    }

    private List<Match> generateAllPermutations(Event event, List<Participant> participants) {
        List<Match> matches = new ArrayList<>();
        int n = participants.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                matches.add(createLeagueMatch(event, participants.get(i), participants.get(j)));
            }
        }
        return matches;
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

    private Match createLeagueMatch(Event event, Participant p1, Participant p2) {
        return Match.builder()
                .event(event)
                .stage("LEAGUE")
                .participantA(p1)
                .participantB(p2)
                .status(MatchStatus.PENDING)
                .build();
    }
}
