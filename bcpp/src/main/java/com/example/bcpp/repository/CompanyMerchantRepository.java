package com.example.bcpp.repository;

import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyMerchantRepository extends JpaRepository<CompanyMerchant, Long> {

    Optional<CompanyMerchant> findByCompanyIdAndMerchantId(Long companyId, Long merchantId);

    boolean existsByCompanyAndMerchant(Company company, Merchant merchant);

}
