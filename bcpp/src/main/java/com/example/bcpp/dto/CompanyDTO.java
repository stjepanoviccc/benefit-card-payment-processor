package com.example.bcpp.dto;

import com.example.bcpp.model.Company;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {

    private Long id;
    @NotBlank
    private String name;

    public static CompanyDTO convertToDto(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .build();
    }

    public Company convertToModel() {
        return Company.builder()
                .id(getId())
                .name(getName())
                .build();
    }

}
