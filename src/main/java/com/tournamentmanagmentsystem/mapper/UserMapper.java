package com.tournamentmanagmentsystem.mapper;

import com.tournamentmanagmentsystem.dto.UserDTO;
import com.tournamentmanagmentsystem.entity.UserEntity;


public class UserMapper {
    private UserMapper() {
        
    }
    public static UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setName(userEntity.getName());
        userDTO.setSurname(userEntity.getSurname());
        return userDTO;
    }
}
