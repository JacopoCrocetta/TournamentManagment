package com.tournamentmanagmentsystem.strategy;

import com.tournamentmanagmentsystem.domain.entity.Event;
import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;

import java.util.List;

/**
 * Strategy interface for generating tournament brackets.
 * Follows the Strategy Pattern to allow different matchmaking algorithms.
 */
public interface MatchmakingStrategy {
    /**
     * Generates a list of matches for the given participants in an event.
     * @param event The event context.
     * @param participants The list of participants (already seeded).
     * @return A list of generated matches.
     */
    List<Match> generateMatches(Event event, List<Participant> participants);
}
