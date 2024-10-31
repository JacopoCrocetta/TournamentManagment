package com.tournamentmanagmentsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tournamentmanagmentsystem.entity.TeamMember;

@Repository
public interface TeamMemberRepository extends CrudRepository<TeamMember, Integer>{
    
}
