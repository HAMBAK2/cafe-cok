package com.sideproject.hororok.utils;


import java.text.NumberFormat;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatConverter {

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

    public static LocalDate convertStringToLocalDate(String dateTimeString) {

        if(dateTimeString.isEmpty()) return null;

        String yearStr = String.valueOf(LocalDateTime.now().getYear()) + "년 ";
        String yearDateTimeStr = yearStr + dateTimeString;

        Pattern pattern = Pattern.compile("(\\d{4})년 (\\d{1,2})월 (\\d{1,2})일");
        Matcher matcher = pattern.matcher(yearDateTimeStr);

        if (!matcher.find()) return null;

        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int dayOfMonth = Integer.parseInt(matcher.group(3));

        return LocalDate.of(year, month, dayOfMonth);
    }

    public static LocalTime convertStringToLocalTime(String dateTimeString) {

        if(dateTimeString.isEmpty()) return null;

        Pattern pattern = Pattern.compile("(\\d{1,2})시 (\\d{1,2})분");
        Matcher matcher = pattern.matcher(dateTimeString);

        if(!matcher.find()) return null;

        int hour = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));

        return LocalTime.of(hour, minute);
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
