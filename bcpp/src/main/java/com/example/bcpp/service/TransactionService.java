package com.example.bcpp.service;

import com.example.bcpp.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO create(Long userId, Long merchantId, String role);
/*
    // helping methods
    void processTransaction(TransactionDTO transactionDTO, User user, Card card, Merchant merchant);

    void processPlatinumTransaction(TransactionDTO transactionDTO, User user, Card card, Merchant merchant, CompanyMerchant companyMerchant);

    Double calculateDiscountedAmount(Double originalAmount, Double discountPercentage); */
}
