package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{}