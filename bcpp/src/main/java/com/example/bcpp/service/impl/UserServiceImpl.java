package com.example.bcpp.service.impl;

import com.example.bcpp.dto.UserDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.User;
import com.example.bcpp.repository.UserRepository;
import com.example.bcpp.service.CardService;
import com.example.bcpp.service.CompanyService;
import com.example.bcpp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.bcpp.dto.UserDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final CardService cardService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getModel(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found.", userId)));
    }

    @Override
    public UserDTO create(UserDTO userDTO, Long companyId) {
        userRepository.findByEmail(userDTO.getEmail())
                .ifPresent(accountRequest -> {
                    throw new BadRequestException(String.format("User with this email: %s is already in database", userDTO.getEmail()));
                });
        User user = userDTO.convertToModel(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCompany(companyService.getModel(companyId));

        User savedUser = userRepository.save(user);
        cardService.create(savedUser);
        return convertToDto(savedUser);
    }
}
