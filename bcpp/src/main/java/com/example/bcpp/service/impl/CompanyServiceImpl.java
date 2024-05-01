package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.repository.CompanyRepository;
import com.example.bcpp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bcpp.dto.CompanyDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public Company getModel(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Company with id %s not found.", id)));
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
