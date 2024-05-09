package com.sideproject.cafe_cok.utils;

import java.text.NumberFormat;
import java.time.*;

public class FormatConverter {

    public static Integer convertSecondsToMinutes(final Integer seconds) {
        return seconds / 60;
    }

    public static String convertToDecimal(int integerValue, int numberOfDigits) {
        String integerString = String.valueOf(integerValue);
        String integerPart = integerString.substring(0, numberOfDigits); // 정수부 추출
        String decimalPart = integerString.substring(numberOfDigits); // 소수부 추출
        String result = integerPart + "." + decimalPart;
        return result;
    }

    public static String priceConvert(Integer price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String formattedPrice = numberFormat.format(price) + "원";
        return formattedPrice;
    }

    public static String convertLocalDateLocalTimeToString(final LocalDate localDate, final LocalTime localTime) {

        StringBuilder convertedDateTime = new StringBuilder();

        if(localDate == null) return convertedDateTime.toString();
        String month = String.valueOf(localDate.getMonthValue());
        String day = String.valueOf(localDate.getDayOfMonth());
        convertedDateTime.append(month + "월");
        convertedDateTime.append(" " + day + "일");

        if(localTime == null) return convertedDateTime.toString();

        String hour = String.valueOf(localTime.getHour());
        String minute = String.valueOf(localTime.getMinute());

        convertedDateTime.append(" " + hour + "시");
        convertedDateTime.append(" " + minute + "분");
        return convertedDateTime.toString();
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
