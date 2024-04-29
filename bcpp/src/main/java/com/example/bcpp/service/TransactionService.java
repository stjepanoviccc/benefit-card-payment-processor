package com.example.bcpp.service;

import com.example.bcpp.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> findAll();
    TransactionDTO create(Long userId, Long merchantId, String role);
}
