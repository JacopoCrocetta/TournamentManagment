package com.tournamentmanagmentsystem.service;


import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.TournamentEntity;
import com.tournamentmanagmentsystem.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TournamentService {

    private TournamentRepository tournamentRepository;

    /**
     * Save a given TournamentEntity. Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public TournamentEntity save(TournamentEntity entity) {
        return this.tournamentRepository.save(entity);
    }

    /**
     * Updates a given TournamentEntity. If the given entity does not have an ID, a empty TournamentEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    public TournamentEntity update(TournamentEntity entity) {
        if(Objects.isNull(entity.getId())){
            return new TournamentEntity();
        }
        return this.tournamentRepository.save(entity);
    }

    /**
     * Retrieves a {@link TournamentEntity} by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<TournamentEntity> findById(int id) {
        return this.tournamentRepository.findById(id);
    }

    /**
     * Deletes the {@link TournamentEntity} with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    public boolean deleteById(int id){
        this.tournamentRepository.deleteById(id);

        return this.tournamentRepository.findById(id).isPresent();
    }
}