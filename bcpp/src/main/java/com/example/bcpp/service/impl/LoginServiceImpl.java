package com.example.bcpp.service.impl;

import com.example.bcpp.dto.LoginRequestDTO;
import com.example.bcpp.dto.LoginResponseDTO;
import com.example.bcpp.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final JwtServiceImpl jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());

        return LoginResponseDTO.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .build();
    }
}
