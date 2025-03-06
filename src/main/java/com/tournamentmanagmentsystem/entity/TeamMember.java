package com.tournamentmanagmentsystem.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TeamMember")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(mappedBy = "team")
    private TeamsEntity team;


    @OneToOne(mappedBy = "user")
    private UserEntity user;
    private String role;
    private LocalDate joinedAt;

    // Getter and Setter for id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for team
    public TeamsEntity getTeam() {
        return this.team;
    }

    public void setTeam(TeamsEntity team) {
        this.team = team;
    }

    // Getter and Setter for user
    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    // Getter and Setter for role
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for joinedAt
    public LocalDate getJoinedAt() {
        return this.joinedAt;
    }

    public void setJoinedAt(LocalDate joinedAt) {
        this.joinedAt = joinedAt;
    }
}
