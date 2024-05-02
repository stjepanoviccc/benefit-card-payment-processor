package com.example.bcpp.dto;

import com.example.bcpp.model.Company;
import com.example.bcpp.model.User;
import com.example.bcpp.model.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private UserType role;
    private Company company;

    public static UserDTO convertToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .company(user.getCompany())
                .role(user.getRole())
                .build();
    }

    public User convertToModel(UserDTO userDTO) {
        return User.builder()
                .id(getId())
                .email(getEmail())
                .password(getPassword())
                .company(getCompany())
                .role(getRole())
                .build();
    }


}
