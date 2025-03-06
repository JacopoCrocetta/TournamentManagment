package com.tournamentmanagmentsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.ParticipantEntity;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;


@Service
public class ParticipantService {

    private ParticipantRepository participantRepository;

    /**
     * Save a given ParticipantEntity. Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public ParticipantEntity save(ParticipantEntity entity) {
        return this.participantRepository.save(entity);
    }

    /**
     * Updates a given ParticipantEntity. If the given entity does not have an ID, a empty
     * ParticipantEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    public ParticipantEntity update(ParticipantEntity entity) {
        return this.participantRepository.save(entity);
    }

    /**
     * Retrieves a {@link ParticipantEntity} by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<ParticipantEntity> findById(int id) {
        return this.participantRepository.findById(id);
    }

     /**
     * Returns a List of all {@link ParticipantEntity} in the repository.
     *
     * @return a List of all {@link ParticipantEntity} in the repository.
     */
    public List<ParticipantEntity> findAll () {
        return StreamSupport.stream(this.participantRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }


    /**
     * Deletes the {@link ParticipantEntity} with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was not deleted, {@literal false} if the entity was successfully deleted.
     */
    public boolean deleteById(int id){
        this.participantRepository.deleteById(id);

        return this.participantRepository.findById(id).isPresent();
    }
}