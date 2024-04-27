package com.example.bcpp.service;

import com.example.bcpp.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();
    UserDTO create(UserDTO userDTO, Long companyId);

}
