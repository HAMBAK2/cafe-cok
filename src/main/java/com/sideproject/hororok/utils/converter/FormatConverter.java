package com.sideproject.hororok.utils.converter;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;

import java.text.NumberFormat;
import java.time.DayOfWeek;

public class FormatConverter {

    public static String priceConvert(Integer price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String formattedPrice = numberFormat.format(price) + "원";
        return formattedPrice;
    }


//    public static String convertToBusiness(CreatePlanSearchCond searchCond) {
//
//        String startTimeString = String.format("%02d:00", searchCond.getStartHour());
//        String endTimeString = String.format("%02d:00", searchCond.getEndHour());
//
//        return searchCond.getDay() + " " + startTimeString + " - " + endTimeString;
//    }

    public static DayOfWeek convertKoreanDayOfWeekToEnglish(String day) {

        System.out.println("데이 출력 " + day);

        switch (day) {
            case "월":
                return DayOfWeek.MONDAY;
            case "화":
                return DayOfWeek.TUESDAY;
            case "수":
                return DayOfWeek.WEDNESDAY;
            case "목":
                return DayOfWeek.THURSDAY;
            case "금":
                return DayOfWeek.FRIDAY;
            case "토":
                return DayOfWeek.SATURDAY;
            case "일":
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("유효하지 않은 요일입니다: " + day);
        }
    }
}
