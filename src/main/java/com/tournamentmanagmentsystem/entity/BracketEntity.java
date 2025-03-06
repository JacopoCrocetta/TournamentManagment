package com.tournamentmanagmentsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
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

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for tournamentId
    public TournamentEntity getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(TournamentEntity tournamentId) {
        this.tournamentId = tournamentId;
    }

    // Getter and Setter for round
    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    // Getter and Setter for match
    public MatchEntity getMatch() {
        return match;
    }

    public void setMatch(MatchEntity match) {
        this.match = match;
    }

    // Getter and Setter for nextMatch
    public MatchEntity getNextMatch() {
        return nextMatch;
    }

    public void setNextMatch(MatchEntity nextMatch) {
        this.nextMatch = nextMatch;
    }
}
