package com.example.bcpp.dto;

import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.model.Merchant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyMerchantDTO {

    private Long id;
    private Company company;
    private Merchant merchant;

    public static CompanyMerchantDTO convertToDto(CompanyMerchant companyMerchant) {
        return CompanyMerchantDTO.builder()
                .id(companyMerchant.getId())
                .company(companyMerchant.getCompany())
                .merchant(companyMerchant.getMerchant())
                .build();
    }

    public CompanyMerchant convertToModel() {
        return CompanyMerchant.builder()
                .id(getId())
                .company(getCompany())
                .merchant(getMerchant())
                .build();
    }
}
