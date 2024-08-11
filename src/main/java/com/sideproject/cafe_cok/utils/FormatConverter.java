package com.sideproject.cafe_cok.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.exception.InvalidCafeException;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.DayOfWeek.*;

@Slf4j
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

    public static DayOfWeek getDyaOfWeekByKoreanDay(String day) {
        switch (day) {
            case "월":
                return MONDAY;
            case "화":
                return TUESDAY;
            case "수":
                return WEDNESDAY;
            case "목":
                return THURSDAY;
            case "금":
                return FRIDAY;
            case "토":
                return SATURDAY;
            case "일":
                return SUNDAY;
            default:
                throw new IllegalArgumentException("유효하지 않은 요일입니다: " + day);
        }
    }

    public static String convertFormatPhoneNumber(final String phoneNumber) {

        if(phoneNumber == null || phoneNumber.isEmpty()) return phoneNumber;
        if(phoneNumber.split("-")[0].equals("0507") || phoneNumber.split("-")[0].equals("0503")) return phoneNumber;

        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, COUNTRY_CODE_KOREA);
            return phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            log.error("전화번호 형식이 올바르지 않습니다. 입력값: {}", phoneNumber);
            throw new InvalidCafeException("전화번호 형식이 올바르지 않습니다.");
        }
    }

    public static File convertBase64StringToFile(final String base64String) {

        byte[] decode = Base64.getDecoder().decode(base64String);
        String extension = detectImageType(decode);
        String filename = UUID.randomUUID().toString() + "." + extension;
        File file = new File(filename);
        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static String detectImageType(byte[] imageData) {
        try (InputStream is = new ByteArrayInputStream(imageData);
             ImageInputStream iis = ImageIO.createImageInputStream(is)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                return reader.getFormatName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
