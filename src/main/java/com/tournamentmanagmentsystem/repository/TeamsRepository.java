package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.TeamsEntity;

@Repository
public interface TeamsRepository extends CrudRepository<TeamsEntity, Integer>{}