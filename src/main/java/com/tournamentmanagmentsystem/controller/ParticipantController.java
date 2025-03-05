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

import com.tournamentmanagmentsystem.entity.ParticipantEntity;
import com.tournamentmanagmentsystem.service.ParticipantService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private ParticipantService participantService;


    /**
     * Retrieves a {@link ParticipantEntity} by its id.
     * 
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    @ApiOperation(value = "Retrieve a participant by its ID", notes = "Retrieves the ParticipantEntity with the given ID if it exists, otherwise an empty Optional")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved"),
        @ApiResponse(code = 404, message = "Participant not found")
    })
    @GetMapping(value = "/find-Participant-by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<ParticipantEntity> findById(@PathVariable int id) {
        return participantService.findById(id);
    }

    /**
     * This method retrieves a list of all Participants.
     * 
     * @return a list of all ParticipantsEntity objects.
     */
    @ApiOperation(value = "Retrieve a list of all participants", notes = "Retrieves a list of all ParticipantEntity objects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved")
    })
    @GetMapping(value = "/find-all-Participant", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParticipantEntity> findAll(){
        return participantService.findAll();
    }

    /**
     * Updates a given ParticipantEntity. If the given entity does not have an ID, a empty ParticipantEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    @ApiOperation(value = "Update a participant", notes = "Updates an existing ParticipantEntity")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated"),
        @ApiResponse(code = 400, message = "Validation error"),
        @ApiResponse(code = 404, message = "Participant not found")
    })
    @PutMapping(value = "/update-Participant", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<ParticipantEntity> updateParticipant(@RequestBody ParticipantEntity entity) {
        return Optional.of(participantService.update(entity));
    }

    /**
     * Creates a new ParticipantEntity.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity wrapped in an Optional, will never be {@literal null}.
     */
    @ApiOperation(value = "Create a participant", notes = "Creates a new ParticipantEntity")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created"),
        @ApiResponse(code = 400, message = "Validation error")
    })
    @PutMapping(value = "/create-Participant", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<ParticipantEntity> createParticipant(@RequestBody ParticipantEntity entity) {
        return Optional.of(participantService.save(entity));
    }

    /**
     * Deletes the {@link ParticipantEntity} with the given id.
     * 
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    @ApiOperation(value = "Delete a participant", notes = "Deletes the ParticipantEntity with the given ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted"),
        @ApiResponse(code = 404, message = "Participant not found")
    })
    @DeleteMapping(value = "/delete-Participant/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteParticipant(@PathVariable int id) {
        return Optional.of(participantService.deleteById(id));
    }
}
