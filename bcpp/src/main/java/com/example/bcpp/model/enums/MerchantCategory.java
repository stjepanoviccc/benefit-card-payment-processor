package com.example.bcpp.model.enums;

import lombok.Getter;

@Getter
public enum MerchantCategory {
    FoodAndDrinks("FOOD AND DRINKS"),
    Recreation("RECREATION"),
    Education("EDUCATION"),
    Culture("CULTURE"),
    Traveling("TRAVELING"),
    Shopping("SHOPPING");

    private final String value;

    MerchantCategory(String value) {
        this.value = value;
    }
}