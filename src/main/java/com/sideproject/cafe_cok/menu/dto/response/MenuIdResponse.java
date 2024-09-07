package com.sideproject.cafe_cok.menu.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class MenuIdResponse extends RepresentationModel<MenuIdResponse> {

    private Long menuId;

    public MenuIdResponse(Long menuId) {
        this.menuId = menuId;
    }
}
