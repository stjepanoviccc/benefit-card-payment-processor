package com.example.bcpp.dto;

import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.model.Merchant;
import com.example.bcpp.model.enums.MerchantCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantDTO {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private MerchantCategory category;
    @NotNull
    private Double price;
    private List<CompanyDTO> companies;

    public static MerchantDTO convertToDto(Merchant merchant, List<CompanyDTO> companyDTOs) {
        return MerchantDTO.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .category(merchant.getCategory())
                .price(merchant.getPrice())
                .companies(companyDTOs)
                .build();
    }

    public static MerchantDTO convertToDto(Merchant merchant) {
        return MerchantDTO.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .category(merchant.getCategory())
                .price(merchant.getPrice())
                .build();
    }

    public Merchant convertToModel() {
        Merchant merchant = Merchant.builder()
                .id(getId())
                .name(getName())
                .category(getCategory())
                .price(getPrice())
                .build();

        if (getCompanies() != null) {
            for (CompanyDTO companyDTO : getCompanies()) {
                merchant.getCompanyMerchants().add(
                        new CompanyMerchant(null, (companyDTO.convertToModel()), merchant));
            }
        }
        return merchant;
    }

}
