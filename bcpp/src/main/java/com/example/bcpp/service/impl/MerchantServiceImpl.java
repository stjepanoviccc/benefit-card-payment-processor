package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.exception.BadRequestException;
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
    private final CompanyMerchantService companyMerchantService;

    @Override
    public List<MerchantDTO> findAll() {
        List<Merchant> merchants = merchantRepository.findAll();
        List<MerchantDTO> merchantDTOs = new ArrayList<>();
        for (Merchant merchant : merchants) {
            List<CompanyDTO> companyDTOs = companyMerchantService.findCompaniesByMerchantId(merchant.getId());
                merchantDTOs.add(convertToDto(merchant, companyDTOs));
        }
        return merchantDTOs;
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
