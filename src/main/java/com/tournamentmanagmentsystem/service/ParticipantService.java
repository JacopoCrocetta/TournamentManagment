package com.tournamentmanagmentsystem.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.ParticipantEntity;
import com.tournamentmanagmentsystem.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
     * Retrieves a {@link ParticipantEntity} by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<ParticipantEntity> findById(int id) {
        return this.participantRepository.findById(id);
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