package com.tournamentmanagmentsystem.entity;

import java.time.LocalDate;

import com.tournamentmanagmentsystem.utility.Utility.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEntity {
    private int id;
    private int userId;
    private String message;
    private Status status;
    private LocalDate sentAt;
}
