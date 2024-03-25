package com.sideproject.hororok.utils.calculator;

import java.math.BigDecimal;

public class GeometricUtils {

    public static boolean isWithinRadius(BigDecimal latitude, BigDecimal longitude, BigDecimal targetLatitude, BigDecimal targetLongitude, BigDecimal radiusInMeters) {
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

        return distance <= radiusInMeters.doubleValue();
    }
}
