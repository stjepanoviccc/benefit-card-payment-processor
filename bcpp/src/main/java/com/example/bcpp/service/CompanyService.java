package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.model.Company;

import java.util.List;

public interface CompanyService {

    Company getModel(Long id);
    List<CompanyDTO> findAll();
    CompanyDTO create(CompanyDTO companyDTO);


}
