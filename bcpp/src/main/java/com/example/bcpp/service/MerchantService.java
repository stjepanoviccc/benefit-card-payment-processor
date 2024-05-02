package com.example.bcpp.service;

import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.model.Merchant;

public interface MerchantService {

    Merchant getModel(Long merchantId);
    MerchantDTO create(MerchantDTO merchantDTO);

}
