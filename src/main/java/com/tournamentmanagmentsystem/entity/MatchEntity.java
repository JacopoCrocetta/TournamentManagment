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

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Match")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int tournamentId;
    private int[] teamIds;
    private int winningTeamsId;
    private int refereeId;
    private LocalDateTime matchTime;
}