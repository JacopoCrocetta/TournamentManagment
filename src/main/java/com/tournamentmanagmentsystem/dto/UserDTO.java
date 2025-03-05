package com.tournamentmanagmentsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String username;
    private String email;
    private String name;
    private String surname;
}
