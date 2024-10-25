package com.tournamentmanagmentsystem.controller;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tournamentmanagmentsystem.entity.TournamentEntity;
import com.tournamentmanagmentsystem.service.TournamentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {

    private TournamentService tournamentService;


    /**
     * This method is used to find a tournament by its ID
     * 
     * @param id the ID of the tournament
     * @return the tournament with the given ID if it exists, otherwise an empty Optional
     */
    @RequestMapping(value = "/find-tournament-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TournamentEntity> findById(@PathVariable int id) {
        return tournamentService.findById(id);
    }
}
