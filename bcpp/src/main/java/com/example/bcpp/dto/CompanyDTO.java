package com.example.bcpp.dto;

import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyMerchant;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {

    private Long id;
    @NotBlank
    private String name;
    private List<MerchantDTO> merchants;

    public static CompanyDTO convertToDto(Company company, List<MerchantDTO> merchantDTOs) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .merchants(merchantDTOs)
                .build();
    }

    public static CompanyDTO convertToDto(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .build();
    }

    public Company convertToModel() {
        Company company = Company.builder()
                .id(getId())
                .name(getName())
                .build();

        if (getMerchants() != null) {
            for (MerchantDTO merchantDTO : getMerchants()) {
                company.getCompanyMerchants().add(
                        new CompanyMerchant(null, company, (merchantDTO.convertToModel())));
            }
        }
        return company;
    }

}
