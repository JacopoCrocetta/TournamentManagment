package com.tournamentmanagmentsystem.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BracketEntity {
    private int id;
    private int tournamentId;
    private int round;
}
