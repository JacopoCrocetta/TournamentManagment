package com.tournamentmanagmentsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "Match")
@ApiModel(description = "Details about the match")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The unique ID of the match")
    private int id;

    @ApiModelProperty(notes = "The ID of the tournament")
    @ManyToOne
    @JoinColumn(name = "matchId")
    private TournamentEntity tournamentEntity;

    @ApiModelProperty(notes = "The IDs of the teams")
    private int[] teamIds;

    @ApiModelProperty(notes = "The ID of the winning team")
    private int winningTeamsId;

    @ApiModelProperty(notes = "The ID of the referee")
    private int refereeId;

    @ApiModelProperty(notes = "The time of the match")
    private LocalDateTime matchTime;

    // Getter and Setter for id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for tournamentEntity
    public TournamentEntity getTournamentEntity() {
        return this.tournamentEntity;
    }

    public void setTournamentEntity(TournamentEntity tournamentEntity) {
        this.tournamentEntity = tournamentEntity;
    }

    // Getter and Setter for teamIds
    public int[] getTeamIds() {
        return this.teamIds;
    }

    public void setTeamIds(int[] teamIds) {
        this.teamIds = teamIds;
    }

    // Getter and Setter for winningTeamsId
    public int getWinningTeamsId() {
        return this.winningTeamsId;
    }

    public void setWinningTeamsId(int winningTeamsId) {
        this.winningTeamsId = winningTeamsId;
    }

    // Getter and Setter for refereeId
    public int getRefereeId() {
        return this.refereeId;
    }

    public void setRefereeId(int refereeId) {
        this.refereeId = refereeId;
    }

    // Getter and Setter for matchTime
    public LocalDateTime getMatchTime() {
        return this.matchTime;
    }

    public void setMatchTime(LocalDateTime matchTime) {
        this.matchTime = matchTime;
    }
}
