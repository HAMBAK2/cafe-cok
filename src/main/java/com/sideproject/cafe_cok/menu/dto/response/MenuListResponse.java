package com.sideproject.cafe_cok.menu.dto.response;

import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MenuListResponse {

    private List<MenuImageUrlDto> menus;

    public MenuListResponse(final List<MenuImageUrlDto> menus) {
        this.menus = menus;
    }
}
