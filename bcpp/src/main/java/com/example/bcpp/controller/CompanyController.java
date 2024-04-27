package com.example.bcpp.controller;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.UserDTO;
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

    private final CompanyService companyService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<CompanyDTO> create(@Valid @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.status(CREATED).body(companyService.create(companyDTO));
    }

    @PostMapping("/{companyId}/createUser")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Long companyId) {
        return ResponseEntity.status(CREATED).body(userService.create(userDTO, companyId));
    }
}
