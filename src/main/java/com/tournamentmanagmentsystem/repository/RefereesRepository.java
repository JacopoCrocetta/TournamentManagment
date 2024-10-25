package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.RefereesEntity;

@Repository
public interface RefereesRepository extends CrudRepository<RefereesEntity, Integer>{}