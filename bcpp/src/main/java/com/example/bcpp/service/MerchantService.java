package com.example.bcpp.service;

import com.example.bcpp.dto.MerchantDTO;

import java.util.List;

public interface MerchantService {

    List<MerchantDTO> findAll();

    MerchantDTO create(MerchantDTO merchantDTO);

}
