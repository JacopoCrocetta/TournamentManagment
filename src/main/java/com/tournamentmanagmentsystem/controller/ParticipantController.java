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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private ParticipantService participantService;


    /**
     * This method is used to find a Participant by its ID
     * 
     * @param id the ID of the Participant
     * @return the Participant with the given ID if it exists, otherwise an empty Optional
     */
    @GetMapping(value = "/find-Participant-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<ParticipantEntity> findById(@PathVariable int id) {
        return participantService.findById(id);
    }

    /**
     * This method retrieves a list of all Participants.
     * 
     * @return a list of all ParticipantsEntity objects.
     */
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
    @DeleteMapping(value = "/delete-Participant", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteParticipant(@PathVariable int id) {
        return Optional.of(participantService.deleteById(id));
    }
}
