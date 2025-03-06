package com.tournamentmanagmentsystem.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tournament")
public class TournamentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String tournamentName;
    private String description;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;

    @OneToOne
    @JoinColumn(name = "id")
    private UserEntity organizer;

    @ManyToMany
    @JoinTable(
        name = "tournament_teams",
        joinColumns = @JoinColumn(name = "tournamentId"),
        inverseJoinColumns = @JoinColumn(name = "tournamentTeamId")
    )
    private Set<TeamsEntity> teams;

    @OneToMany(mappedBy = "Matches")
    private List<MatchEntity> matches;

    @OneToOne
    @JoinColumn(name = "id")
    private StandingsEntity standing;

    @OneToOne
    @JoinColumn(name = "tournamentId")
    private BracketEntity bracket;

    private String tournamentType;

    // Getter and Setter for id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for tournamentName
    public String getTournamentName() {
        return this.tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    // Getter and Setter for description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for beginDate
    public LocalDateTime getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    // Getter and Setter for endDate
    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    // Getter and Setter for organizer
    public UserEntity getOrganizer() {
        return this.organizer;
    }

    public void setOrganizer(UserEntity organizer) {
        this.organizer = organizer;
    }

    // Getter and Setter for teams
    public Set<TeamsEntity> getTeams() {
        return this.teams;
    }

    public void setTeams(Set<TeamsEntity> teams) {
        this.teams = teams;
    }

    // Getter and Setter for matches
    public List<MatchEntity> getMatches() {
        return this.matches;
    }

    public void setMatches(List<MatchEntity> matches) {
        this.matches = matches;
    }

    // Getter and Setter for standing
    public StandingsEntity getStanding() {
        return this.standing;
    }

    public void setStanding(StandingsEntity standing) {
        this.standing = standing;
    }

    // Getter and Setter for bracket
    public BracketEntity getBracket() {
        return this.bracket;
    }

    public void setBracket(BracketEntity bracket) {
        this.bracket = bracket;
    }

    // Getter and Setter for tournamentType
    public String getTournamentType() {
        return this.tournamentType;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }
}
