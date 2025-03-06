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

import com.tournamentmanagmentsystem.entity.RefereesEntity;
import com.tournamentmanagmentsystem.service.RefereesService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/Referees")
public class RefereesController {

    private RefereesService refereesService;


    /**
     * Retrieves a {@link RefereesEntity} by its id.
     * 
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    @ApiOperation(value = "Retrieve a Referees by its ID", notes = "Retrieves the RefereesEntity with the given ID if it exists, otherwise an empty Optional")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved"),
        @ApiResponse(code = 404, message = "Referees not found")
    })
    @GetMapping(value = "/find-Referees-by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<RefereesEntity> findById(@PathVariable int id) {
        return refereesService.findById(id);
    }

    /**
     * Retrieves a list of all Referees.
     * 
     * @return a list of all RefereesEntity objects.
     */
    @ApiOperation(value = "Retrieve a list of all referees", notes = "Retrieves a list of all RefereesEntity objects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved")
    })
    @GetMapping(value = "/find-all-Referees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RefereesEntity> findAll(){
        return refereesService.findAll();
    }

    /**
     * Updates a given RefereesEntity. If the given entity does not have an ID, a empty RefereesEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    @ApiOperation(value = "Update a Referees", notes = "Updates an existing RefereesEntity, if the entity does not have an ID, a empty RefereesEntity is returned")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated"),
        @ApiResponse(code = 400, message = "Validation error"),
        @ApiResponse(code = 404, message = "Referees not found")
    })
    @PutMapping(value = "/update-Referees", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<RefereesEntity> updateReferees(@RequestBody RefereesEntity entity) {
        return Optional.of(refereesService.update(entity));
    }

    /**
     * Creates a new RefereesEntity.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity wrapped in an Optional, will never be {@literal null}.
     */
    @ApiOperation(value = "Create a Referees", notes = "Creates a new RefereesEntity")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created"),
        @ApiResponse(code = 400, message = "Validation error")
    })
    @PutMapping(value = "/create-Referees", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<RefereesEntity> createReferees(@RequestBody RefereesEntity entity) {
        return Optional.of(refereesService.save(entity));
    }

    /**
     * Deletes the {@link RefereesEntity} with the given id.
     * 
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    @ApiOperation(value = "Delete a referee", notes = "Deletes the RefereesEntity with the given ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted"),
        @ApiResponse(code = 404, message = "Referee not found")
    })
    @DeleteMapping(value = "/delete-Referees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteReferees(@PathVariable int id) {
        return Optional.of(refereesService.deleteById(id));
    }
}
