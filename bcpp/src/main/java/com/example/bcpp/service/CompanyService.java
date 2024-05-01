package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.model.Company;

public interface CompanyService {

    Company getModel(Long id);
    CompanyDTO create(CompanyDTO companyDTO);


}
