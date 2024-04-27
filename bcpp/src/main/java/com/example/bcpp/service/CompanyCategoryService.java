package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyCategoryDTO;

public interface CompanyCategoryService {

    CompanyCategoryDTO create(String merchantCategory, Long companyId);

}
