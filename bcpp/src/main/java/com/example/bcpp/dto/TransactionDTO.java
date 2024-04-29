package com.example.bcpp.dto;

import com.example.bcpp.model.Merchant;
import com.example.bcpp.model.Transaction;
import com.example.bcpp.model.User;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.model.enums.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private Long id;
    private User user;
    private Double amount;
    private LocalDateTime date;
    private TransactionStatus status;
    private Merchant merchant;

    public static TransactionDTO convertToDto(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .user(transaction.getUser())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .status(transaction.getStatus())
                .merchant(transaction.getMerchant())
                .build();
    }

    public Transaction convertToModel() {
        return Transaction.builder()
                .id(getId())
                .user(getUser())
                .amount(getAmount())
                .date(getDate())
                .status(getStatus())
                .merchant(getMerchant())
                .build();
    }
}
