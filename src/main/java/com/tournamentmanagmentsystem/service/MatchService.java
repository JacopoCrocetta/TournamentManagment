package com.tournamentmanagmentsystem.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.MatchEntity;
import com.tournamentmanagmentsystem.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    /**
     * Save a given MatchEntity. Use the returned instance for further operations as
     * the save operation might have changed the entity instance completely.
     * 
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public MatchEntity save(MatchEntity entity) {
        return this.matchRepository.save(entity);
    }

    /**
     * Retrieves a {@link MatchEntity} by its id.
     * 
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<MatchEntity> findById(int id) {
        return this.matchRepository.findById(id);
    }

    /**
     * Deletes the {@link MatchEntity} with the given id.
     * 
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    public boolean deleteById(int id){
        this.matchRepository.deleteById(id);

        return this.matchRepository.findById(id).isPresent();
    }
}