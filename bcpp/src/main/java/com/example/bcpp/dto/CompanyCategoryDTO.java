package com.example.bcpp.dto;

import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyCategory;
import com.example.bcpp.model.enums.MerchantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCategoryDTO {

    private Long id;
    private Company company;
    private MerchantCategory merchantCategory;

    public static CompanyCategoryDTO convertToDto(CompanyCategory companyCategory) {
        return CompanyCategoryDTO.builder()
                .id(companyCategory.getId())
                .company(companyCategory.getCompany())
                .merchantCategory(companyCategory.getMerchantCategory())
                .build();
    }

    public CompanyCategory convertToModel() {
        return CompanyCategory.builder()
                .id(getId())
                .company(getCompany())
                .merchantCategory(getMerchantCategory())
                .build();
    }

}
