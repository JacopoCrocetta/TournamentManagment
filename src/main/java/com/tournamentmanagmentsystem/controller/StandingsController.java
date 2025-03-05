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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
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
    @GetMapping(value = "/find-Standings-by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve a Standings by its ID", notes = "Retrieves the StandingsEntity with the given ID if it exists, otherwise an empty Optional")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Standings not found")
    })
    public Optional<StandingsEntity> findById(@PathVariable int id) {
        return standingsService.findById(id);
    }

    /**
     * This method retrieves a list of all Standings.
     * 
     * @return a list of all StandingsEntity objects.
     */
    @GetMapping(value = "/find-all-Standings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve a list of all Standings", notes = "Retrieves a list of all StandingsEntity objects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved")
    })
    public List<StandingsEntity> findAll(){
        return standingsService.findAll();
    }

    /**
     * Updates a given StandingsEntity. If the given entity does not have an ID, a empty StandingsEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    @ApiOperation(value = "Update a Standings", notes = "Updates an existing StandingsEntity, if the entity does not have an ID, an empty StandingsEntity is returned")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated"),
        @ApiResponse(code = 400, message = "Validation error"),
        @ApiResponse(code = 404, message = "Standings not found")
    })
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
    @ApiOperation(value = "Create a Standings", notes = "Creates a new StandingsEntity")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created"),
        @ApiResponse(code = 400, message = "Validation error")
    })
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
    @ApiOperation(value = "Delete a Standings", notes = "Deletes the StandingsEntity with the given ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted"),
        @ApiResponse(code = 404, message = "Standings not found")
    })
    @DeleteMapping(value = "/delete-Standings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteStandings(@PathVariable int id) {
        return Optional.of(standingsService.deleteById(id));
    }
}
