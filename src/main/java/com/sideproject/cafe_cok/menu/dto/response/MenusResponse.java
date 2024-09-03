package com.sideproject.cafe_cok.menu.dto.response;

import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MenusResponse {

    private List<MenuImageUrlDto> menus;

    public MenusResponse(final List<MenuImageUrlDto> menus) {
        this.menus = menus;
    }
}
