package com.tournamentmanagmentsystem.entity;

import java.time.LocalDate;


public class Subscription {
    private int id;
    private int tournamentId;
    private int teamId;
    private int userId;
    private LocalDate registrationDate;
    

    // Getter and Setter for id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for tournamentId
    public int getTournamentId() {
        return this.tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    // Getter and Setter for teamId
    public int getTeamId() {
        return this.teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    // Getter and Setter for userId
    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter for registrationDate
    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}
