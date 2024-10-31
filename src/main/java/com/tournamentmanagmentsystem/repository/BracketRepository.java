package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.BracketEntity;

@Repository
public interface BracketRepository extends CrudRepository<BracketEntity, Integer> {}