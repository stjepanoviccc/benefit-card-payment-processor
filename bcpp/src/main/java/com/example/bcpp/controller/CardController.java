package com.example.bcpp.controller;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.bcpp.dto.CardDTO.convertToDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    @GetMapping("/{cardId}")
    @PreAuthorize("hasAnyRole('ROLE_Standard', 'ROLE_Premium', 'ROLE_Platinum')")
    public ResponseEntity<CardDTO> getCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(convertToDto(cardService.getModel(cardId)));
    }

    @PatchMapping("/{cardId}/update/{amount}/status/{cardUpdateStatus}/users/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_Standard', 'ROLE_Premium', 'ROLE_Platinum')")
    public ResponseEntity<CardDTO> updateTotalAmount(@PathVariable Long cardId, @PathVariable Double amount, @PathVariable String cardUpdateStatus,
        @PathVariable Long userId) {
        return ResponseEntity.ok(cardService.update(cardId, amount, cardUpdateStatus, userId));
    }

}
