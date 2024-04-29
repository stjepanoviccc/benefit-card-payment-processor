package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.CompanyMerchantDTO;
import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.model.Merchant;
import com.example.bcpp.repository.CompanyMerchantRepository;
import com.example.bcpp.repository.CompanyRepository;
import com.example.bcpp.repository.MerchantRepository;
import com.example.bcpp.service.CompanyMerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.example.bcpp.dto.CompanyMerchantDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class CompanyMerchantServiceImpl implements CompanyMerchantService {

    private final CompanyRepository companyRepository;
    private final MerchantRepository merchantRepository;
    private final CompanyMerchantRepository companyMerchantRepository;

    @Override
    public CompanyMerchant getModel(Long companyId, Long merchantId) {
        // it can be null
        return companyMerchantRepository.findByCompanyIdAndMerchantId(companyId, merchantId);
    }

    @Override
    public List<MerchantDTO> findMerchantsByCompanyId(Long id) {
        List<MerchantDTO> merchants = companyMerchantRepository.findMerchantsByCompanyId(id);
        return merchants != null ? merchants : Collections.emptyList();
    }

    @Override
    public List<CompanyDTO> findCompaniesByMerchantId(Long id) {
        List<CompanyDTO> companies = companyMerchantRepository.findCompaniesByMerchantId(id);
        return companies != null ? companies : Collections.emptyList();
    }

    @Override
    public CompanyMerchantDTO create(Long companyId, Long merchantId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException(String.format("Company with id %s not found.", companyId)));
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException(String.format("Merchant with id %s not found.", merchantId)));

        boolean alreadyExists = companyMerchantRepository.existsByCompanyAndMerchant(company, merchant);

        if (alreadyExists) {
            throw new BadRequestException("Company is already connected to this merchant");
        }

        CompanyMerchant companyMerchant = new CompanyMerchant();
        companyMerchant.setCompany(company);
        companyMerchant.setMerchant(merchant);
        return convertToDto(companyMerchantRepository.save(companyMerchant));
    }
}
