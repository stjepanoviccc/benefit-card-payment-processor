package com.example.bcpp.service;

import com.example.bcpp.dto.UserDTO;
import com.example.bcpp.model.User;

import java.util.List;

public interface UserService {

    User getModel(Long userId);
    List<UserDTO> findAll();
    UserDTO create(UserDTO userDTO, Long companyId);

}
