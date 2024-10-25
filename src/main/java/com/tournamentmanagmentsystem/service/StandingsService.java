package com.tournamentmanagmentsystem.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.StandingsEntity;
import com.tournamentmanagmentsystem.repository.StandingsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StandingsService {

    private StandingsRepository standingsRepository;

    /**
     * Save a given StandingsEntity. Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public StandingsEntity save(StandingsEntity entity) {
        return this.standingsRepository.save(entity);
    }

    /**
     * Retrieves a {@link StandingsEntity} by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<StandingsEntity> findById(int id) {
        return this.standingsRepository.findById(id);
    }

    /**
     * Deletes the {@link StandingsEntity} with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    public boolean deleteById(int id){
        this.standingsRepository.deleteById(id);

        return this.standingsRepository.findById(id).isPresent();
    }
}