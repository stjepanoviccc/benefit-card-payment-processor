package com.example.bcpp.service;

import com.example.bcpp.dto.DiscountDTO;
import com.example.bcpp.model.Discount;

public interface DiscountService {
    Discount getModel(Long companyMerchantId);
    DiscountDTO create(DiscountDTO discountDTO, Long companyMerchantId);
}
