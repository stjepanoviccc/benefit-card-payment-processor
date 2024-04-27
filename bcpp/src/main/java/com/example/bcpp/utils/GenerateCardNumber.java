package com.example.bcpp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateCardNumber {

    public static String generateCardNumber() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String formattedTime = dateFormat.format(new Date(currentTimeMillis));
        int randomDigits = ThreadLocalRandom.current().nextInt(10000, 99999);

        return formattedTime + randomDigits;
    }

}
