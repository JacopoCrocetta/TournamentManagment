package com.tournamentmanagmentsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "Bracket")
public class BracketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(mappedBy = "tournament")
    private TournamentEntity tournamentId;
    private int round;

    @OneToOne
    @JoinColumn(name = "id")
    private MatchEntity match;

    @OneToOne
    @JoinColumn(name = "id")
    private MatchEntity nextMatch;
}
