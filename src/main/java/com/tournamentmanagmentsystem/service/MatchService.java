package com.tournamentmanagmentsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.MatchEntity;
import com.tournamentmanagmentsystem.repository.MatchRepository;


@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

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
     * Updates a given MatchEntity. If the given entity does not have an ID, a empty
     * MatchEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    public MatchEntity update(MatchEntity entity) {
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
     * Returns a List of all {@link MatchEntity} in the repository.
     *
     * @return a List of all {@link MatchEntity} in the repository.
     */
    public List<MatchEntity> findAll () {
        return StreamSupport.stream(this.matchRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
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