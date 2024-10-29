package com.tournamentmanagmentsystem.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultEntity {
    private int id;
    private int matchId;
    private int teamId;
    private int score;
    private int duration;
}
