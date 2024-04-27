package com.example.bcpp.controller;

import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping()
    public ResponseEntity<MerchantDTO> create(@Valid @RequestBody MerchantDTO merchantDTO) {
        return ResponseEntity.status(CREATED).body(merchantService.create(merchantDTO));
    }
}
