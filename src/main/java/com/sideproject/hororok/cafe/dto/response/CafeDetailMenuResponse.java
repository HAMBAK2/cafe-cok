package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.menu.dto.MenuDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailMenuResponse {

    private List<MenuDto> menus;

    public static CafeDetailMenuResponse from(final List<MenuDto> menus) {
        return CafeDetailMenuResponse.builder()
                .menus(menus)
                .build();
    }

}
