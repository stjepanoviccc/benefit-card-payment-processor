package com.example.bcpp.service.impl;

import com.example.bcpp.dto.DiscountDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.repository.CompanyMerchantRepository;
import com.example.bcpp.repository.DiscountRepository;
import com.example.bcpp.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bcpp.dto.DiscountDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final CompanyMerchantRepository companyMerchantRepository;

    @Override
    public DiscountDTO create(DiscountDTO discountDTO, Long companyMerchantId) {
        boolean existsByCompanyMerchant = discountRepository.existsByCompanyMerchantId(companyMerchantId);
        if (existsByCompanyMerchant) {
            throw new BadRequestException("Discount already exists for this company and merchant");
        }
        CompanyMerchant companyMerchant = companyMerchantRepository.findById(companyMerchantId)
                .orElseThrow(() -> new NotFoundException(String.format("CompanyMerchant with id %s not found.", companyMerchantId)));
        discountDTO.setCompanyMerchant(companyMerchant);

        return convertToDto(discountRepository.save(discountDTO.convertToModel()));
    }
}
