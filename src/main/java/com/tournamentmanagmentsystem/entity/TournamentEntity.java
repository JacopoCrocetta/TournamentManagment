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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Tournament")
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
}
