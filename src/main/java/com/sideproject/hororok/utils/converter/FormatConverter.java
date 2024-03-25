package com.sideproject.hororok.utils.converter;

import java.text.NumberFormat;

public class FormatConverter {

    public static String priceConvert(Integer price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String formattedPrice = numberFormat.format(price) + "원";
        return formattedPrice;
    }
}
