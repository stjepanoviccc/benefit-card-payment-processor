package com.example.bcpp.service;

import com.example.bcpp.dto.LoginRequestDTO;
import com.example.bcpp.dto.LoginResponseDTO;

public interface LoginService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
