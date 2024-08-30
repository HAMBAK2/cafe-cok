package com.sideproject.cafe_cok.utils;

import java.math.BigDecimal;

import static com.sideproject.cafe_cok.utils.Constants.*;


public class GeometricUtils {

    private static final double WALK_SPEED = 5;

    public static double calculateRadiusInKm(final Integer minutes) {
        double walkingSpeedMeterPerHour = WALK_SPEED * 1000.0;
        double hours = (double) minutes / 60.0;
        double distanceInMeter = walkingSpeedMeterPerHour * hours;
        double distanceInKm = distanceInMeter / 1000.0;
        return distanceInKm;
    }
}
