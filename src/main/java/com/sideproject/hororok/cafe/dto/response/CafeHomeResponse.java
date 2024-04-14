package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.category.dto.CategoryKeywords;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CafeHomeResponse {

    private final List<String> purpose;
    private final List<String> atmosphere;
    private final List<String> facility;
    private final List<String> menu;
    private final List<String> theme;

    public static CafeHomeResponse from(final CategoryKeywords categoryKeywords) {
        return CafeHomeResponse.builder()
                .purpose(categoryKeywords.getPurpose())
                .atmosphere(categoryKeywords.getAtmosphere())
                .facility(categoryKeywords.getFacility())
                .menu(categoryKeywords.getMenu())
                .theme(categoryKeywords.getTheme())
                .build();
    }
}
