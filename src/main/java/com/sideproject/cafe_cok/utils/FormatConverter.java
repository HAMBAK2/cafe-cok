package com.sideproject.cafe_cok.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import jakarta.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormatConverter {

    private static final String COUNTRY_CODE_KOREA = "KR";

    public static Integer convertSecondsToMinutes(final Integer seconds) {
        return seconds / 60;
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

    public static String convertFormatPhoneNumber(final String phoneNumber) {

        if(phoneNumber == null || phoneNumber.isEmpty()) return phoneNumber;

        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, COUNTRY_CODE_KOREA);
            return phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File convertBase64StringToFile(final String base64String) {

        String[] splitedString = base64String.split(",");

        String extension = splitedString[0].split(";")[0].split("/")[1];
        byte[] imageData = DatatypeConverter.parseBase64Binary(splitedString[1]);
        String filename = UUID.randomUUID().toString() + "." + extension;
        File file = new File(filename);
        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageData);
            System.out.println("Image file saved as " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static String changePath(String originPath, final String originDirName, final String newDirName) {

        return originPath.replace(originDirName, newDirName);
    }

    public static List<List<String>> convertOperationHoursToListString(final List<OperationHour> operationHours){
        List<List<String>> hours = new ArrayList<>();

        for (OperationHour operationHour : operationHours) {
            List<String> innerList = new ArrayList<>();
            LocalTime openingTime = operationHour.getOpeningTime();
            LocalTime closingTime = operationHour.getClosingTime();

            if(openingTime.equals(LocalTime.of(0, 0))
                    && closingTime.equals(LocalTime.of(0, 0))) {
                innerList.add("");
                innerList.add("");
            } else {
                innerList.add(openingTime.toString());
                innerList.add(closingTime.toString());
            }
            hours.add(innerList);
        }
        return hours;
    }

    public static String convertOperationHourToString(final OperationHour operationHour) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String date = getKoreanDayOfWeek(operationHour.getDate());
        String openingTime = operationHour.getOpeningTime().format(formatter);
        String closingTime = operationHour.getClosingTime().format(formatter);
        return date + " " + openingTime + "~" + closingTime;
    }

}
