package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyCategoryDTO;
import com.example.bcpp.model.CompanyCategory;
import com.example.bcpp.model.enums.MerchantCategory;

public interface CompanyCategoryService {

    CompanyCategory getModel(Long companyId, MerchantCategory category);
    CompanyCategoryDTO create(String merchantCategory, Long companyId);

}
