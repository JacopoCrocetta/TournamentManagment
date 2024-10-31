package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.MatchResultEntity;

@Repository
public interface MatchResultRepository extends CrudRepository<MatchResultEntity, Integer> {}