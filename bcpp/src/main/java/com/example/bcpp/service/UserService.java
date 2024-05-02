package com.example.bcpp.service;

import com.example.bcpp.dto.UserDTO;
import com.example.bcpp.model.User;

public interface UserService {

    User getModel(Long userId);
    UserDTO create(UserDTO userDTO, Long companyId);

}
