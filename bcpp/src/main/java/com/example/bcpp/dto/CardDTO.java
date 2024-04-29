package com.example.bcpp.dto;

import com.example.bcpp.model.Card;
import com.example.bcpp.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {

    private Long id;
    @NotNull
    private User user;
    @NotBlank
    private String cardNumber;
    @NotBlank
    private Double totalAmount;

    public static CardDTO convertToDto(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .user(card.getUser())
                .cardNumber(card.getCardNumber())
                .totalAmount(card.getTotalAmount())
                .build();
    }

    public Card convertToModel() {
        return Card.builder()
                .id(getId())
                .user(getUser())
                .cardNumber(getCardNumber())
                .totalAmount(getTotalAmount())
                .build();
    }
}
