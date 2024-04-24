package com.sideproject.hororok.utils.converter;


import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class FormatConverter {

    public static String priceConvert(Integer price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String formattedPrice = numberFormat.format(price) + "원";
        return formattedPrice;
    }

    public static String dateTimeConvert(LocalDate date, LocalTime time) {
        String month = String.valueOf(date.getMonthValue());
        String day = String.valueOf(date.getDayOfMonth());
        String hour = String.valueOf(time.getHour());

        return month + "월 " + day + "일 " + hour + "시";
    }

    public static String getKoreanDayOfWeek(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return "월";
            case TUESDAY:
                return "화";
            case WEDNESDAY:
                return "수";
            case THURSDAY:
                return "목";
            case FRIDAY:
                return "금";
            case SATURDAY:
                return "토";
            case SUNDAY:
                return "일";
            default:
                throw new IllegalArgumentException("유효하지 않은 요일입니다: " + day);
        }
    }

}
