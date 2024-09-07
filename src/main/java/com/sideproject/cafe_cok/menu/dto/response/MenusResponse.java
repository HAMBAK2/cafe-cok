package com.sideproject.cafe_cok.menu.dto.response;

import com.sideproject.cafe_cok.menu.dto.MenuImageDto;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
public class MenusResponse extends RepresentationModel<MenusResponse> {

    private List<MenuImageDto> menus;

    public MenusResponse(final List<MenuImageDto> menus) {
        this.menus = menus;
    }
}
