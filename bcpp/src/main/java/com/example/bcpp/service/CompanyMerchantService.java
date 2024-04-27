package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.CompanyMerchantDTO;
import com.example.bcpp.dto.MerchantDTO;

import java.util.List;

public interface CompanyMerchantService {
    List<MerchantDTO> findMerchantsByCompanyId(Long id);
    List<CompanyDTO> findCompaniesByMerchantId(Long id);
    CompanyMerchantDTO create(Long companyId, Long merchantId);
}