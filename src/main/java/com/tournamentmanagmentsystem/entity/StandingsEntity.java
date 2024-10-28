package com.tournamentmanagmentsystem.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
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
}
