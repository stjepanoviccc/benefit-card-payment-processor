package com.example.bcpp.repository;

import com.example.bcpp.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    boolean existsByCompanyMerchantId(Long companyMerchantId);

    Discount findByCompanyMerchantId(Long companyMerchantId);
}
