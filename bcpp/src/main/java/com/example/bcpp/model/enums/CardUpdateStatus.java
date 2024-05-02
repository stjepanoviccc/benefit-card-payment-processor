package com.example.bcpp.model.enums;

public enum CardUpdateStatus {
    Increase("INCREASE"),
    Decrease("DECREASE");

    private final String value;

    CardUpdateStatus(String value) {
        this.value = value;
    }
}
