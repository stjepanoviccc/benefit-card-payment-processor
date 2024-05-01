package com.example.bcpp.controller;

import com.example.bcpp.dto.TransactionDTO;
import com.example.bcpp.service.JwtService;
import com.example.bcpp.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtService jwtService;

    @PostMapping("/users/{userId}/merchants/{merchantId}")
    @PreAuthorize("hasAnyRole('ROLE_Standard', 'ROLE_Premium', 'ROLE_Platinum')")
    public ResponseEntity<TransactionDTO> create(@PathVariable Long userId, @PathVariable Long merchantId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.replace("Bearer ", "");
        String role = jwtService.getRole(token);
        return ResponseEntity.status(CREATED).body(transactionService.create(userId, merchantId, role));
    }
}
