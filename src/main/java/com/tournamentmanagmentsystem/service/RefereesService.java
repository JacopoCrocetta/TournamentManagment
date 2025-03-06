package com.tournamentmanagmentsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.tournamentmanagmentsystem.entity.RefereesEntity;
import com.tournamentmanagmentsystem.repository.RefereesRepository;


@Service
public class RefereesService {

    private RefereesRepository refereesRepository;

    /**
     * Save a given RefereesEntity. Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public RefereesEntity save(RefereesEntity entity) {
        return this.refereesRepository.save(entity);
    }

    /**
     * Updates a given RefereesEntity. If the given entity does not have an ID, a empty
     * RefereesEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    public RefereesEntity update(RefereesEntity entity) {
        return this.refereesRepository.save(entity);
    }

    /**
     * Retrieves a {@link RefereesEntity} by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<RefereesEntity> findById(int id) {
        return this.refereesRepository.findById(id);
    }

     /**
     * Returns a List of all {@link RefereesEntity} in the repository.
     *
     * @return a List of all {@link RefereesEntity} in the repository.
     */
    public List<RefereesEntity> findAll () {
        return StreamSupport.stream(this.refereesRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }

    /**
     * Deletes the {@link RefereesEntity} with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was not deleted, {@literal false} if the entity was successfully deleted.
     */
    public boolean deleteById(int id){
        this.refereesRepository.deleteById(id);

        return this.refereesRepository.findById(id).isPresent();
    }
}