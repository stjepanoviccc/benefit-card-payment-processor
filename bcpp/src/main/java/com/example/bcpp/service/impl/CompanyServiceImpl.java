package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.repository.CompanyRepository;
import com.example.bcpp.service.CompanyMerchantService;
import com.example.bcpp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.bcpp.dto.CompanyDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMerchantService companyMerchantService;

    @Override
    public Company getModel(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Company with id %s not found.", id)));
    }

    @Override
    public List<CompanyDTO> findAll() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyDTO> companyDTOs = new ArrayList<>();
        for (Company company : companies) {
            List<MerchantDTO> merchantDTOs = companyMerchantService.findMerchantsByCompanyId(company.getId());
                    companyDTOs.add(convertToDto(company, merchantDTOs));
        }
        return companyDTOs;
    }

    @Override
    public CompanyDTO create(CompanyDTO companyDTO) {
        companyRepository.findByName(companyDTO.getName())
                .ifPresent(dto -> {
                    throw new BadRequestException(String.format("Company with this name: %s is already in database", companyDTO.getName()));
                });
        return convertToDto(companyRepository.save(companyDTO.convertToModel()));
    }
}
