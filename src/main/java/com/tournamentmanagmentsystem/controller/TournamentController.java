package com.tournamentmanagmentsystem.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {

    private TournamentService tournamentService;
    private static final Logger logger = LogManager.getLogger(TournamentController.class);


    /**
     * This method is used to find a tournament by its ID
     * 
     * @param id the ID of the tournament
     * @return the tournament with the given ID if it exists, otherwise an empty Optional
     */
    @GetMapping(value = "/find-tournament-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve a tournament by its ID", notes = "Retrieves the TournamentEntity with the given ID if it exists, otherwise an empty Optional")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Tournament not found")
    })
    public Optional<TournamentEntity> findById(@PathVariable int id) {
        logger.info("Searching tournament with id: %d", id);
        return tournamentService.findById(id);
    }

    /**
     * This method retrieves a list of all tournaments.
     * 
     * @return a list of all TournamentEntity objects.
     */
    @ApiOperation(value = "Retrieve a list of all tournaments", notes = "Retrieves a list of all TournamentEntity objects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved")
    })
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
    @ApiOperation(value = "Update a tournament", notes = "Updates an existing TournamentEntity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 404, message = "Tournament not found")
    })
    public Optional<TournamentEntity> updateTournament(@RequestBody TournamentEntity entity) {
        return Optional.of(tournamentService.update(entity));
    }

    /**
     * Creates a new TournamentEntity.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity wrapped in an Optional, will never be {@literal null}.
     */
    @ApiOperation(value = "Create a tournament", notes = "Creates a new TournamentEntity")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "Validation error")
    })
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
    @ApiOperation(value = "Delete a tournament", notes = "Deletes the TournamentEntity with the given ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted"),
        @ApiResponse(code = 404, message = "Tournament not found")
    })
    @DeleteMapping(value = "/delete-tournament", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteTournament(@PathVariable int id) {
        return Optional.of(tournamentService.deleteById(id));
    }
}
