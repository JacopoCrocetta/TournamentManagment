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

import com.tournamentmanagmentsystem.entity.TeamsEntity;
import com.tournamentmanagmentsystem.service.TeamsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Teams")
@RequiredArgsConstructor
public class TeamsController {

    private TeamsService teamsService;


    /**
     * This method is used to find a Teams by its ID
     * 
     * @param id the ID of the Teams
     * @return the Teams with the given ID if it exists, otherwise an empty Optional
     */
    @ApiOperation(value = "Retrieve a Teams by its ID", notes = "Retrieves the TeamsEntity with the given ID if it exists, otherwise an empty Optional")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Teams not found")
    })
    @GetMapping(value = "/find-Teams-by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TeamsEntity> findById(@PathVariable int id) {
        return teamsService.findById(id);
    }

    /**
     * Retrieves a list of all Teams.
     * 
     * @return a list of all TeamsEntity objects.
     */
    @ApiOperation(value = "Retrieve a list of all teams", notes = "Retrieves a list of all TeamsEntity objects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved")
    })
    @GetMapping(value = "/find-all-Teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TeamsEntity> findAll(){
        return teamsService.findAll();
    }

    /**
     * Updates a given TeamsEntity. If the given entity does not have an ID, a empty TeamsEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    @PutMapping(value = "/update-Teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TeamsEntity> updateTeams(@RequestBody TeamsEntity entity) {
        return Optional.of(teamsService.update(entity));
    }

    /**
     * Creates a new TeamsEntity.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity wrapped in an Optional, will never be {@literal null}.
     */
    @ApiOperation(value = "Create a Teams", notes = "Creates a new TeamsEntity")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created"),
        @ApiResponse(code = 400, message = "Validation error")
    })
    @PutMapping(value = "/create-Teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<TeamsEntity> createTeams(@RequestBody TeamsEntity entity) {
        return Optional.of(teamsService.save(entity));
    }

    /**
     * Deletes the {@link TeamsEntity} with the given id.
     * 
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    @DeleteMapping(value = "/delete-Teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteTeams(@PathVariable int id) {
        return Optional.of(teamsService.deleteById(id));
    }
}
