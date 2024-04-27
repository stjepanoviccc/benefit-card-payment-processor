package com.example.bcpp.model.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    Successful("SUCCESSFUL"),
    Unsuccessful("UNSUCCESSFUL");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }
}