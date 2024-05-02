package com.example.bcpp.controller;

import com.example.bcpp.dto.DiscountDTO;
import com.example.bcpp.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/companyMerchant/{companyMerchantId}")
    public ResponseEntity<DiscountDTO> create(@Valid @RequestBody DiscountDTO discountDTO, @PathVariable Long companyMerchantId) {
        return ResponseEntity.status(CREATED).body(discountService.create(discountDTO, companyMerchantId));
    }
}
