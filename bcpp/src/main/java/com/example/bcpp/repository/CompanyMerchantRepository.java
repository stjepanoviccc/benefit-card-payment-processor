package com.example.bcpp.repository;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyMerchantRepository extends JpaRepository<CompanyMerchant, Long> {

    @Query(nativeQuery = true, value = "SELECT m.* " +
            "FROM merchant m " +
            "INNER JOIN company_merchant cm ON cm.merchant_id = m.id " +
            "WHERE cm.company_id = :companyId;")
    List<MerchantDTO> findMerchantsByCompanyId(Long companyId);

    @Query(nativeQuery = true, value= "SELECT c.* " +
            "FROM company c " +
            "INNER JOIN company_merchant cm ON cm.company_id = c.id " +
            "WHERE cm.merchant_id = :merchantId;")
    List<CompanyDTO> findCompaniesByMerchantId(Long merchantId);

    boolean existsByCompanyAndMerchant(Company company, Merchant merchant);
}
