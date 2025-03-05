package com.tournamentmanagmentsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tournamentmanagmentsystem.entity.MatchEntity;
import com.tournamentmanagmentsystem.repository.MatchRepository;

@SpringBootTest
@Transactional
class MatchServiceTest {
    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchRepository matchRepository;

    @Test
    void testSaveMatch() {
        MatchEntity match = new MatchEntity();
        match.setTeamIds(new int[] {1, 2});

        MatchEntity savedMatch = matchService.save(match);

        assertEquals(match.getTeamIds()[0], savedMatch.getTeamIds()[0]);
        assertEquals(match.getTeamIds()[1], savedMatch.getTeamIds()[1]);
    }

    @Test
    void testFindById() {
        MatchEntity match = new MatchEntity();
        match.setTeamIds(new int[] {1, 2});

        match = matchRepository.save(match);

        Optional<MatchEntity> foundMatch = matchService.findById(match.getId());

        assertTrue(foundMatch.isPresent());
        assertEquals(match.getId(), foundMatch.get().getId());
    }

    @Test
    void testDeleteById() {
        MatchEntity match = new MatchEntity();

        match.setTeamIds(new int[] {1, 2});

        match = matchRepository.save(match);

        boolean isDeleted = matchService.deleteById(match.getId());

        assertTrue(isDeleted);
        assertTrue(matchRepository.findById(match.getId()).isEmpty());
    }
}
