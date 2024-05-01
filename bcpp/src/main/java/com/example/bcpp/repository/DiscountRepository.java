package com.example.bcpp.repository;

import com.example.bcpp.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    boolean existsByCompanyMerchantId(Long companyMerchantId);
    Optional<Discount> findByCompanyMerchantId(Long companyMerchantId);
}
