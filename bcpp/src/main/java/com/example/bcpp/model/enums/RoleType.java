package com.example.bcpp.model.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_Standard("ROLE_Standard"),
    ROLE_Premium("ROLE_Premium"),
    ROLE_Platinum("ROLE_Platinum");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

}