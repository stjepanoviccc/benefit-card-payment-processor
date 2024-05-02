package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyMerchantDTO;
import com.example.bcpp.model.CompanyMerchant;

public interface CompanyMerchantService {
    CompanyMerchant getModel(Long companyId, Long merchantId);
    CompanyMerchantDTO create(Long companyId, Long merchantId);
}
