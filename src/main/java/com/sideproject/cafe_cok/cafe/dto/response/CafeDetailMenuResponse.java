package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CafeDetailMenuResponse {

    private List<MenuImageUrlDto> menus;

    public CafeDetailMenuResponse(final List<MenuImageUrlDto> menus) {
        this.menus = menus;
    }
}
