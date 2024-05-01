package com.example.bcpp.service;

import com.example.bcpp.dto.UserDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.User;
import com.example.bcpp.model.enums.UserType;
import com.example.bcpp.repository.UserRepository;
import com.example.bcpp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.bcpp.dto.UserDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private final User user = createUser(1L, "test_email_1@gmail.com");

    private final User user2 = createUser(2L, "test_email_2@gmail.com");

    private User createUser(Long id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .password("password")
                .role(UserType.Standard)
                .build();
    }

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CompanyService companyService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldGetUser_whenGetModel_ifUserExists() {
        // Given: Mocking getModel when User exists.
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO userDto = convertToDto(userService.getModel(user.getId()));

        assertNotNull(userDto);
        assertEquals(user.getEmail(), userDto.getEmail());

        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifUserDoesNotExist() {
        // Given: Mocking when User doesn't exist.
        when(userRepository.findById(user2.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> userService.getModel(user2.getId()));

        assertEquals(String.format("User with id %s not found.", user2.getId()), exception.getMessage());

        verify(userRepository).findById(user2.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldCreateUser_whenCreate_ifUserDoesNotExist() {
        // Given: Mocking when User doesn't exist.
        UserDTO userDTO = UserDTO.convertToDto(user);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Verify is it created successful
        UserDTO createdUserDTO = userService.create(userDTO, anyLong());
        assertEquals(user.getEmail(), createdUserDTO.getEmail());
        assertEquals(user.getPassword(), createdUserDTO.getPassword());
        assertEquals(user.getRole(), createdUserDTO.getRole());

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreate_ifUserExists() {
        // Given: Mocking existing User
        UserDTO existingUserDTO = UserDTO.convertToDto(user2);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUserDTO.convertToModel(existingUserDTO)));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.create(existingUserDTO, anyLong()));

        assertEquals(String.format("User with this email: %s is already in database", user2.getEmail()), exception.getMessage());

        verify(userRepository).findByEmail(anyString());
        verify(userRepository, never()).save(any());
    }
}
