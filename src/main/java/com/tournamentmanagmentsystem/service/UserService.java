package com.tournamentmanagmentsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

 import com.tournamentmanagmentsystem.entity.UserEntity;
import com.tournamentmanagmentsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    /**
     * Save a given UserEntity. Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    public UserEntity save(UserEntity entity) {
        return this.userRepository.save(entity);
    }

     /**
     * Updates a given UserEntity. If the given entity does not have an ID, a empty
     * UserEntity is returned.
     * 
     * @param entity must not be {@literal null}.
     * @return the updated entity will never be {@literal null}.
     */
    public UserEntity update(UserEntity entity) {
        return this.userRepository.save(entity);
    }


    /**
     * Retrieves a {@link UserEntity} by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    public Optional<UserEntity> findById(int id) {
        return this.userRepository.findById(id);
    }

    /**
     * Returns a List of all {@link UserEntity} in the repository.
     *
     * @return a List of all {@link UserEntity} in the repository.
     */
    public List<UserEntity> findAll () {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false).toList();
    }


    /**
     * Deletes the {@link UserEntity} with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if the entity was deleted, {@literal false} otherwise.
     */
    public boolean deleteById(int id){
        this.userRepository.deleteById(id);

        return this.userRepository.findById(id).isPresent();
    }
}