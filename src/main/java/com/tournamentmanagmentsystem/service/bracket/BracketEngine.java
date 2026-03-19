package com.tournamentmanagmentsystem.service.bracket;

import org.springframework.lang.Nullable;

import com.tournamentmanagmentsystem.domain.entity.Match;
import com.tournamentmanagmentsystem.domain.entity.Participant;

import java.util.List;
import java.util.UUID;

/**
 * Interface for bracket generation strategies.
 */
public interface BracketEngine {

    /**
     * Generates the initial matches for a tournament event.
     * 
     * @param eventId      The ID of the event.
     * @param participants The list of confirmed participants.
     * @return A list of generated matches.
     */
    List<Match> generateInitialMatches(UUID eventId, List<Participant> participants);

    /**
     * Advances winners to the next stage/round.
     * 
     * @param finishedMatch The match that just finished.
     * @return The next match where the winner advanced, or null.
     */
    @Nullable
    Match advanceWinner(Match finishedMatch);
}
