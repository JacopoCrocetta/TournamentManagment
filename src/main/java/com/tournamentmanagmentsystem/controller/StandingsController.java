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

import com.tournamentmanagmentsystem.entity.StandingsEntity;
import com.tournamentmanagmentsystem.service.StandingsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/standings")
@RequiredArgsConstructor
public class StandingsController {

    private StandingsService standingsService;


    /**
     * This method is used to find a Standings by its ID
     * 
     * @param id the ID of the Standings
     * @return the Standings with the given ID if it exists, otherwise an empty Optional
     */
    @GetMapping(value = "/find-Standings-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<StandingsEntity> findById(@PathVariable int id) {
        return standingsService.findById(id);
    }

    /**
     * This method retrieves a list of all Standings.
     * 
     * @return a list of all StandingsEntity objects.
     */
    @GetMapping(value = "/find-all-Standings", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StandingsEntity> findAll(){
        return standingsService.findAll();
    }

    /**
     * Updates a given StandingsEntity. If the given entity does not have an ID, a empty StandingsEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    @PutMapping(value = "/update-Standings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<StandingsEntity> updateStandings(@RequestBody StandingsEntity entity) {
        return Optional.of(standingsService.update(entity));
    }

    /**
     * Creates a new StandingsEntity.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity wrapped in an Optional, will never be {@literal null}.
     */
    @PutMapping(value = "/create-Standings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<StandingsEntity> createStandings(@RequestBody StandingsEntity entity) {
        return Optional.of(standingsService.save(entity));
    }

    /**
     * Deletes the {@link StandingsEntity} with the given id.
     * 
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    @DeleteMapping(value = "/delete-Standings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteStandings(@PathVariable int id) {
        return Optional.of(standingsService.deleteById(id));
    }
}
