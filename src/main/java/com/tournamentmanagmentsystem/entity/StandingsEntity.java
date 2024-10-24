package com.tournamentmanagmentsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Standings")
public class StandingsEntity {
    private int id;
    private int points;
    private int ranking;

    private TeamsEntity[] teams;
    private TournamentEntity tournament;
}