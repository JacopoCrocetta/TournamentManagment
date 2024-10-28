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

import com.tournamentmanagmentsystem.entity.MatchEntity;
import com.tournamentmanagmentsystem.service.MatchService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private MatchService matchService;


    /**
     * This method is used to find a match by its ID
     * 
     * @param id the ID of the match
     * @return the match with the given ID if it exists, otherwise an empty Optional
     */
    @ApiOperation(value = "Retrieve a match by its ID", notes = "Retrieves the MatchEntity with the given ID if it exists, otherwise an empty Optional")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved"),
        @ApiResponse(code = 404, message = "Match not found")
    })
    @GetMapping(value = "/find-match-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<MatchEntity> findById(@PathVariable int id) {
        return matchService.findById(id);
    }

    /**
     * Retrieves a List of all {@link MatchEntity} in the repository.
     * 
     * @return a List of all {@link MatchEntity} in the repository.
     */
    @ApiOperation(value = "Retrieve a list of all matches", notes = "Retrieves a list of all MatchEntity objects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved")
    })
    @GetMapping(value = "/find-all-match", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MatchEntity> findAll(){
        return matchService.findAll();
    }

    /**
     * Updates a given MatchEntity. If the given entity does not have an ID, an empty MatchEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    @ApiOperation(value = "Update a match", notes = "Updates an existing MatchEntity")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated"),
        @ApiResponse(code = 400, message = "Validation error"),
        @ApiResponse(code = 404, message = "Match not found")
    })
    @PutMapping(value = "/update-match", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<MatchEntity> updateMatch(@RequestBody MatchEntity entity) {
        return Optional.of(matchService.update(entity));
    }

    /**
     * Creates a new MatchEntity.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity wrapped in an Optional, will never be {@literal null}.
     */
    @ApiOperation(value = "Create a match", notes = "Creates a new MatchEntity")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created"),
        @ApiResponse(code = 400, message = "Validation error")
    })
    @PutMapping(value = "/create-match", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<MatchEntity> createMatch(@RequestBody MatchEntity entity) {
        return Optional.of(matchService.save(entity));
    }

    /**
     * Deletes the {@link MatchEntity} with the given id.
     * 
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    @ApiOperation(value = "Delete a match", notes = "Deletes the MatchEntity with the given ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted"),
        @ApiResponse(code = 404, message = "Match not found")
    })
    @DeleteMapping(value = "/delete-match", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteMatch(@PathVariable int id) {
        return Optional.of(matchService.deleteById(id));
    }
}
