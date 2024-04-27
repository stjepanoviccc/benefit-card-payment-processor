package com.example.bcpp.controller;

import com.example.bcpp.dto.CompanyCategoryDTO;
import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.CompanyMerchantDTO;
import com.example.bcpp.dto.UserDTO;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.service.CompanyCategoryService;
import com.example.bcpp.service.CompanyMerchantService;
import com.example.bcpp.service.CompanyService;
import com.example.bcpp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyCategoryService companyCategoryService;
    private final CompanyService companyService;
    private final UserService userService;
    private final CompanyMerchantService companyMerchantService;

    @PostMapping()
    public ResponseEntity<CompanyDTO> create(@Valid @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.status(CREATED).body(companyService.create(companyDTO));
    }

    @PostMapping("/{companyId}/createUser")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Long companyId) {
        return ResponseEntity.status(CREATED).body(userService.create(userDTO, companyId));
    }

    @PostMapping("/{companyId}/merchants/{merchantId}")
    public ResponseEntity<CompanyMerchantDTO> create(@PathVariable Long companyId, @PathVariable Long merchantId) {
        return ResponseEntity.status(CREATED).body(companyMerchantService.create(companyId, merchantId));
    }

    @PostMapping("/{companyId}/categories/{merchantCategory}")
    public ResponseEntity<CompanyCategoryDTO> addCategory(@PathVariable MerchantCategory merchantCategory, @PathVariable Long companyId) {
        return ResponseEntity.status(CREATED).body(companyCategoryService.create(String.valueOf(merchantCategory), companyId));
    }
}
