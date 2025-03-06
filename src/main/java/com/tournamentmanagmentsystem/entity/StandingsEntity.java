package com.tournamentmanagmentsystem.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Standings")
@ApiModel(description = "Details about the standings")
public class StandingsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The unique ID of the standings")
    private int id;

    @ApiModelProperty(notes = "The points of the standings")
    private int points;

    @ApiModelProperty(notes = "The ranking of the standings")
    private int ranking;

    @ApiModelProperty(notes = "The teams associated with the standings")
    private TeamsEntity[] teams;

    @ApiModelProperty(notes = "The tournament associated with the standings")
    private TournamentEntity tournament;

    // Getter and Setter for id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for points
    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // Getter and Setter for ranking
    public int getRanking() {
        return this.ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    // Getter and Setter for teams
    public TeamsEntity[] getTeams() {
        return this.teams;
    }

    public void setTeams(TeamsEntity[] teams) {
        this.teams = teams;
    }

    // Getter and Setter for tournament
    public TournamentEntity getTournament() {
        return this.tournament;
    }

    public void setTournament(TournamentEntity tournament) {
        this.tournament = tournament;
    }
}
