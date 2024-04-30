package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Merchant;
import com.example.bcpp.repository.MerchantRepository;
import com.example.bcpp.service.CompanyMerchantService;
import com.example.bcpp.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.bcpp.dto.MerchantDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;

    @Override
    public Merchant getModel(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException(String.format("Merchant with id %s not found.", merchantId)));
    }

    @Override
    public MerchantDTO create(MerchantDTO merchantDTO) {
        merchantRepository.findByName(merchantDTO.getName())
                .ifPresent(dto -> {
                    throw new BadRequestException(String.format("Merchant with this name: %s is already in database", merchantDTO.getName()));
                });
        return convertToDto(merchantRepository.save(merchantDTO.convertToModel()));
    }
}
