package com.tournamentmanagmentsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Referees")
public class RefereesEntity {
    private int id;
    private String nome;
}