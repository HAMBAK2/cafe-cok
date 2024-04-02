package com.sideproject.hororok.utils.converter;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;

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


    public static String convertVisitDateTime(CreatePlanSearchCond searchCond) {

        LocalDate date = LocalDate.parse(searchCond.getDate());
        LocalTime startTime = searchCond.getStartTime();

        return date.getMonthValue()+"월 " + date.getDayOfMonth()+"일 " +
                getKoreanDayOfWeek(date.getDayOfWeek()) + "요일 " +
                startTime.getHour() + "시 " + startTime.getMinute()+"분";
    }

}
