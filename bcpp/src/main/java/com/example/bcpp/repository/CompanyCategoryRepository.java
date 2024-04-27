package com.example.bcpp.repository;

import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyCategory;
import com.example.bcpp.model.enums.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyCategoryRepository extends JpaRepository<CompanyCategory, Long> {
    boolean existsByCompanyAndMerchantCategory(Company company, MerchantCategory category);
}
