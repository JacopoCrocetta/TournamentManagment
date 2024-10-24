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
@Table(name = "Tournament")
@Data
@NoArgsConstructor
@Getter
@Setter
public class TournamentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String tournamentName;
    private String description;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;

    private ParticipantEntity participant;

    private String tournamentType;
}