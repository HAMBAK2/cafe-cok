package com.sideproject.cafe_cok.util;


public class GeometricUtil {

    private static final double WALK_SPEED = 5;

    public static double calculateRadiusInKm(final Integer minutes) {
        double walkingSpeedMeterPerHour = WALK_SPEED * 1000.0;
        double hours = (double) minutes / 60.0;
        double distanceInMeter = walkingSpeedMeterPerHour * hours;
        double distanceInKm = distanceInMeter / 1000.0;
        return distanceInKm;
    }
}
