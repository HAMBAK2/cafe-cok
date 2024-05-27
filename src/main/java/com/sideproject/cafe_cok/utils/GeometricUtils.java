package com.sideproject.cafe_cok.utils;

import java.math.BigDecimal;

import static com.sideproject.cafe_cok.utils.Constants.*;


public class GeometricUtils {

    private static final double WALK_SPEED = 5;

    public static boolean isWithinRadius(
            final BigDecimal latitude, final BigDecimal longitude,
            final BigDecimal targetLatitude, final BigDecimal targetLongitude,
            final Double radius) {
        double R = 6371.0; // 지구 반지름 (단위: km)
        double lat1 = Math.toRadians(latitude.doubleValue());
        double lon1 = Math.toRadians(longitude.doubleValue());
        double lat2 = Math.toRadians(targetLatitude.doubleValue());
        double lon2 = Math.toRadians(targetLongitude.doubleValue());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // 거리 계산 결과를 미터 단위로 변환

        return distance <= radius;
    }

    public static double calculateDistanceInMeter(final Integer minutes) {
        double walkingSpeedMeterPerHour = WALK_SPEED * 1000.0;
        double hours = (double) minutes / 60.0;
        double distanceInMeter = walkingSpeedMeterPerHour * hours;
        return distanceInMeter;
    }

    public static double calculateRadiusInKm(final Integer minutes) {
        double walkingSpeedMeterPerHour = WALK_SPEED * 1000.0;
        double hours = (double) minutes / 60.0;
        double distanceInMeter = walkingSpeedMeterPerHour * hours;
        double distanceInKm = distanceInMeter / 1000.0;
        return distanceInKm;
    }


    public static double calculateDistanceInKm(final BigDecimal lat1,
                                               final BigDecimal lon1,
                                               final BigDecimal lat2,
                                               final BigDecimal lon2) {

        double radLat1 = Math.toRadians(lat1.doubleValue());
        double radLon1 = Math.toRadians(lon1.doubleValue());
        double radLat2 = Math.toRadians(lat2.doubleValue());
        double radLon2 = Math.toRadians(lon2.doubleValue());

        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return distance;
    }
}
