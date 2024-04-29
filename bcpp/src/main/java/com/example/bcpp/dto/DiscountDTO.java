package com.example.bcpp.dto;

import com.example.bcpp.model.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDTO {

    private Long id;
    private CompanyMerchant companyMerchant;
    @NotNull
    private Double discountPercentage;

    public static DiscountDTO convertToDto(Discount discount) {
        return DiscountDTO.builder()
                .id(discount.getId())
                .companyMerchant(discount.getCompanyMerchant())
                .discountPercentage(discount.getDiscountPercentage())
                .build();
    }

    public Discount convertToModel() {
        return Discount.builder()
                .id(getId())
                .companyMerchant(getCompanyMerchant())
                .discountPercentage(getDiscountPercentage())
                .build();
    }

}
