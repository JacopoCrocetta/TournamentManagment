package com.tournamentmanagmentsystem.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMember {
    private int id;
    private int teamId;
    private int userId;
    private String role;
    private LocalDate joinedAt;
}
