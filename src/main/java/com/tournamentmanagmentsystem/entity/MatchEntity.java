package com.tournamentmanagmentsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Match")
@ApiModel(description = "Details about the match")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The unique ID of the match")
    private int id;

    @ApiModelProperty(notes = "The ID of the tournament")
    private int tournamentId;

    @ApiModelProperty(notes = "The IDs of the teams")
    private int[] teamIds;

    @ApiModelProperty(notes = "The ID of the winning team")
    private int winningTeamsId;

    @ApiModelProperty(notes = "The ID of the referee")
    private int refereeId;

    @ApiModelProperty(notes = "The time of the match")
    private LocalDateTime matchTime;
}
