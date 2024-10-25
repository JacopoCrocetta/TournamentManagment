package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.StandingsEntity;

@Repository
public interface StandingsRepository extends CrudRepository<StandingsEntity, Integer>{}