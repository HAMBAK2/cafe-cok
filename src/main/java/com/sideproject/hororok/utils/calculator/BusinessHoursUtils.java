package com.sideproject.hororok.utils.calculator;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.utils.converter.FormatConverter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


public class BusinessHoursUtils {

    private static final String OPEN = "영업중";
    private static final String CLOSE = "영업종료";
    private static final String EVERYDAY = "매일";



    public static String getBusinessStatus(String businessHours) {

        LocalDateTime now = LocalDateTime.now();
        if(!isBusinessHours(businessHours)) return CLOSE;

        LocalTime[] splitTime = splitToTime(businessHours);
        if(now.toLocalTime().isAfter(splitTime[0]) && now.toLocalTime().isBefore(splitTime[1])) {
                    return OPEN;
        } else return CLOSE;
    }

    public static boolean isBusinessHours(String businessHours) {

        if(!businessHours.substring(0, 2).equals("매일")) {
            List<DayOfWeek> dayOfWeeks = splitToDay(businessHours);
            LocalDateTime now = LocalDateTime.now();
            DayOfWeek today = now.getDayOfWeek();

            int startDayCompareResult = dayOfWeeks.get(0).compareTo(today);
            int endDayCompareResult = dayOfWeeks.get(1).compareTo(today);


            if(startDayCompareResult > 0) return false;
            if(endDayCompareResult < 0) return false;

            return true;
        }

        return true;
    }
//
//    public static boolean isBusinessHours(CreatePlanSearchCond searchCond, Cafe cafe) {
//
//        //휴무일과 받은 날짜가 일치하는 경우
//        if(cafe.getClosedDay() != null && cafe.getClosedDay().contains(searchCond.getDay())) {
//            return false;
//        }
//
//        LocalTime[] splitTimes = splitToTime(cafe.getBusinessHours());
//
//        if(isWithinTimeRange(splitTimes,
//                searchCond.getStartHour().getHour(), searchCond.getEndHour().getHour())) {
//            return true;
//        }
//        return false;
//    }

    private static boolean isWithinTimeRange(LocalTime[] timeRange, Integer startHour, Integer endHour) {

        if(startHour < timeRange[0].getHour() || startHour > timeRange[1].getHour()) return false;
        if(endHour < timeRange[0].getHour() || endHour > timeRange[1].getHour()) return false;

        return true;
    }

    public static List<String> closedDaysConvert(String closedDays) {

        List<String> convertList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < closedDays.length(); i++) {
            if(closedDays.charAt(i) == ',' || closedDays.charAt(i) == ' ') continue;

            sb.append(closedDays.charAt(i));
            if(sb.length() == 3) {
                convertList.add(sb.toString());
                sb = new StringBuilder();
            }
        }

        return convertList;
    }


    private static List<DayOfWeek> splitToDay(String businessHours) {

        String substring = businessHours.substring(0, 3);
        String[] split = substring.split("~");

        DayOfWeek startDay = FormatConverter.convertKoreanDayOfWeekToEnglish(split[0]);
        DayOfWeek endDay = FormatConverter.convertKoreanDayOfWeekToEnglish(split[1]);

        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        dayOfWeeks.add(startDay);
        dayOfWeeks.add(endDay);

        return dayOfWeeks;
    }

    private static LocalTime[] splitToTime(String businessHours) {

        String substring = businessHours.substring(3, businessHours.length());
        String[] split = substring.split("~");

        if(split[1].trim().equals("24:00")) split[1] = "23:59";

        return new LocalTime[]{LocalTime.parse(split[0].trim()), LocalTime.parse(split[1].trim())};
    }



}
