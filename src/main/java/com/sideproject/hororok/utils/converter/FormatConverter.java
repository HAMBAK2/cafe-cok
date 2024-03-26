package com.sideproject.hororok.utils.converter;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;

import java.text.NumberFormat;

public class FormatConverter {

    public static String priceConvert(Integer price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String formattedPrice = numberFormat.format(price) + "원";
        return formattedPrice;
    }


    public static String convertToBusiness(CreatePlanSearchCond searchCond) {

        String startTimeString = String.format("%02d:00", searchCond.getStartHour());
        String endTimeString = String.format("%02d:00", searchCond.getEndHour());

        return searchCond.getDay() + " " + startTimeString + " - " + endTimeString;
    }
}
