package com.sideproject.hororok.utils.calculator;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.utils.converter.FormatConverter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessHoursUtils {

    private static final String OPEN = "영업중";
    private static final String CLOSE = "영업종료";

    public static String getBusinessStatus(String businessHours) {

        Map<String, LocalTime[]> businessHourMap = split(businessHours);
        LocalDateTime now = LocalDateTime.now();
        String today = getKoreaDay(now.getDayOfWeek());

        for (String day : businessHourMap.keySet()) {
            if(day.equals(today)) {
                LocalTime[] localTimes = businessHourMap.get(day);

                if(now.toLocalTime().isAfter(localTimes[0]) && now.toLocalTime().isBefore(localTimes[1])) {
                    return OPEN;
                } else {
                    return CLOSE;
                }
            }
        }

        return CLOSE;
    }

    public static boolean isBusinessHours(CreatePlanSearchCond searchCond, Cafe cafe) {

        Map<String, LocalTime[]> businessHourMap = split(cafe.getBusinessHours());

        for (String day : businessHourMap.keySet()) {
            if(day.equals(searchCond.getDay()) &&
                    isWithinTimeRange(businessHourMap.get(day), searchCond.getStartHour(), searchCond.getEndHour())) {
                return true;
            }
        }

        return false;
    }

    private static boolean isWithinTimeRange(LocalTime[] timeRange, Integer startHour, Integer endHour) {

        if(startHour >= timeRange[0].getHour() && startHour <= timeRange[1].getHour()) return true;
        if(endHour >= timeRange[0].getHour() && endHour <= timeRange[1].getHour()) return true;

        return false;
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



    private static Map<String, LocalTime[]> split(String businessHours) {

        String[] splitBusinessHours = businessHours.split(", ");
        Map<String, LocalTime[]> businessHourMap = new HashMap<>();

        for (String splitBusinessHour : splitBusinessHours) {
            String[] split = splitBusinessHour.split(" ");
            String day = split[0];
            LocalTime startTime = LocalTime.parse(split[1]);
            LocalTime endTime = LocalTime.parse(split[3]);
            businessHourMap.put(day, new LocalTime[]{startTime, endTime});
        }

        return businessHourMap;
    }

    private static String getKoreaDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "월요일";
            case TUESDAY:
                return "화요일";
            case WEDNESDAY:
                return "수요일";
            case THURSDAY:
                return "목요일";
            case FRIDAY:
                return "금요일";
            case SATURDAY:
                return "토요일";
            case SUNDAY:
                return "일요일";
            default:
                throw new IllegalArgumentException("Invalid dayOfWeek: " + dayOfWeek);
        }
    }
}
