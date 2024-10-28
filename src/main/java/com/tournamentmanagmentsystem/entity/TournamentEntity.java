package com.tournamentmanagmentsystem.entity;

import java.time.LocalDateTime;

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
@Table(name = "Tournament")
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "A tournament")
public class TournamentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The unique ID of the tournament")
    private int id;
    @ApiModelProperty(notes = "The name of the tournament")
    private String tournamentName;
    @ApiModelProperty(notes = "The description of the tournament")
    private String description;
    @ApiModelProperty(notes = "The begin date of the tournament")
    private LocalDateTime beginDate;
    @ApiModelProperty(notes = "The end date of the tournament")
    private LocalDateTime endDate;

    private ParticipantEntity participant;

    @ApiModelProperty(notes = "The type of the tournament")
    private String tournamentType;
}
