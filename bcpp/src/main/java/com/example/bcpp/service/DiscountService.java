package com.example.bcpp.service;

import com.example.bcpp.dto.DiscountDTO;

public interface DiscountService {
    DiscountDTO create(DiscountDTO discountDTO, Long companyMerchantId);
}
