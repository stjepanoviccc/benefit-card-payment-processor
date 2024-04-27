package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CompanyCategoryDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyCategory;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.repository.CompanyCategoryRepository;
import com.example.bcpp.repository.CompanyRepository;
import com.example.bcpp.service.CompanyCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bcpp.dto.CompanyCategoryDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class CompanyCategoryServiceImpl implements CompanyCategoryService {

    private final CompanyCategoryRepository companyCategoryRepository;
    private final CompanyRepository companyRepository;

    @Override
    public CompanyCategoryDTO create(String merchantCategory, Long companyId) {
        MerchantCategory category = MerchantCategory.valueOf(merchantCategory);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException(String.format("Company with id %s not found.", companyId)));
        CompanyCategory companyCategory = new CompanyCategory();

        boolean alreadyExists = companyCategoryRepository.existsByCompanyAndMerchantCategory(company, category);
        if (alreadyExists) {
            throw new BadRequestException("Company is already connected to this merchant category");
        }

        companyCategory.setCompany(company);
        companyCategory.setMerchantCategory(category);
        return convertToDto(companyCategoryRepository.save(companyCategory));
    }
}
