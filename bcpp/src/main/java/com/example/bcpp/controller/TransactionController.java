package com.example.bcpp.controller;

import com.example.bcpp.dto.TransactionDTO;
import com.example.bcpp.service.JwtService;
import com.example.bcpp.service.TransactionService;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtService jwtService;

    @GetMapping()
    public ResponseEntity<List<TransactionDTO>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @PostMapping("/users/{userId}/merchants/{merchantId}")
    @PreAuthorize("hasAnyRole('ROLE_Standard', 'ROLE_Premium', 'ROLE_Platinum')")
    public ResponseEntity<TransactionDTO> create(@PathVariable Long userId, @PathVariable Long merchantId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.replace("Bearer ", "");
        String role = jwtService.getRole(token);
        return ResponseEntity.status(CREATED).body(transactionService.create(userId, merchantId, role));
    }
}
