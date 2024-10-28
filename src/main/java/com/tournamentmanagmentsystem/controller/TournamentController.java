package com.tournamentmanagmentsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @GetMapping(value = "/find-tournament-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TournamentEntity> findById(@PathVariable int id) {
        return tournamentService.findById(id);
    }

    /**
     * This method retrieves a list of all tournaments.
     * 
     * @return a list of all TournamentEntity objects.
     */
    @GetMapping(value = "/find-all-tournament", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TournamentEntity> findAll(){
        return tournamentService.findAll();
    }

    /**
     * Updates a given TournamentEntity. If the given entity does not have an ID, a empty TournamentEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    @PutMapping(value = "/update-tournament", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TournamentEntity> updateTournament(@RequestBody TournamentEntity entity) {
        return Optional.of(tournamentService.update(entity));
    }

    /**
     * Creates a new TournamentEntity.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity wrapped in an Optional, will never be {@literal null}.
     */
    @PutMapping(value = "/create-tournament", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TournamentEntity> createTournament(@RequestBody TournamentEntity entity) {
        return Optional.of(tournamentService.save(entity));
    }

    /**
     * Deletes the {@link TournamentEntity} with the given id.
     * 
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    @DeleteMapping(value = "/delete-tournament", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteTournament(@PathVariable int id) {
        return Optional.of(tournamentService.deleteById(id));
    }
}
