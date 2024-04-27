package com.example.bcpp.model.enums;

import lombok.Getter;

@Getter
public enum UserType {
    Standard("STANDARD"),
    Premium("PREMIUM"),
    Platinum("PLATINUM");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

}
