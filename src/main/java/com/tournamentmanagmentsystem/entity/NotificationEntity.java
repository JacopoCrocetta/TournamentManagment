package com.tournamentmanagmentsystem.entity;


import com.tournamentmanagmentsystem.utility.NotificationType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEntity {
    
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  private UserEntity user;
  
  @Enumerated(EnumType.STRING)
  private NotificationType type;
  
  private String message;
}
