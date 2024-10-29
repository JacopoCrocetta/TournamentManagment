package com.tournamentmanagmentsystem.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subscription {
    private int id;
    private int tournamentId;
    private int teamId;
    private int userId;
    private LocalDate registrationDate;
    
}
