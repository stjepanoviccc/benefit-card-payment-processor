package com.example.bcpp.model.enums;

import lombok.Getter;

@Getter
public enum MerchantCategory {
    FoodAndDrinks(0L, "FOOD AND DRINKS"),
    Recreation(1L, "RECREATION"),
    Education(2L, "EDUCATION"),
    Culture(3L, "CULTURE"),
    Traveling(4L, "TRAVELING"),
    Shopping(5L, "SHOPPING");

    @Getter
    private final Long id;
    private final String value;

    MerchantCategory(Long id, String value) {
        this.id = id;
        this.value = value;
    }

}