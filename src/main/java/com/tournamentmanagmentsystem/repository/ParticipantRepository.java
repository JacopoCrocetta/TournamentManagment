package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.ParticipantEntity;

@Repository
public interface ParticipantRepository extends CrudRepository<ParticipantEntity, Integer>{}