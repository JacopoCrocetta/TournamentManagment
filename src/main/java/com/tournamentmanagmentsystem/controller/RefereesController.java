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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Referees")
@RequiredArgsConstructor
public class RefereesController {

    private RefereesService refereesService;


    /**
     * This method is used to find a Referees by its ID
     * 
     * @param id the ID of the Referees
     * @return the Referees with the given ID if it exists, otherwise an empty Optional
     */
    @GetMapping(value = "/find-Referees-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<RefereesEntity> findById(@PathVariable int id) {
        return refereesService.findById(id);
    }

    /**
     * This method retrieves a list of all Referees.
     * 
     * @return a list of all RefereesEntity objects.
     */
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
    @DeleteMapping(value = "/delete-Referees", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Boolean> deleteReferees(@PathVariable int id) {
        return Optional.of(refereesService.deleteById(id));
    }
}
