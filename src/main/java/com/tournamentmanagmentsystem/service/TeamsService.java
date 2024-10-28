package com.tournamentmanagmentsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.TeamsEntity;
import com.tournamentmanagmentsystem.repository.TeamsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamsService {

    private TeamsRepository teamsRepository;

    /**
     * Save a given TeamsEntity. Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public TeamsEntity save(TeamsEntity entity) {
        return this.teamsRepository.save(entity);
    }

     /**
     * Updates a given TeamsEntity. If the given entity does not have an ID, a empty
     * TeamsEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    public TeamsEntity update(TeamsEntity entity) {
        return this.teamsRepository.save(entity);
    }


    /**
     * Retrieves a {@link TeamsEntity} by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<TeamsEntity> findById(int id) {
        return this.teamsRepository.findById(id);
    }

    /**
     * Returns a List of all {@link TeamsEntity} in the repository.
     *
     * @return a List of all {@link TeamsEntity} in the repository.
     */
    public List<TeamsEntity> findAll () {
        return StreamSupport.stream(this.teamsRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }


    /**
     * Deletes the {@link TeamsEntity} with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    public boolean deleteById(int id){
        this.teamsRepository.deleteById(id);

        return this.teamsRepository.findById(id).isPresent();
    }
}