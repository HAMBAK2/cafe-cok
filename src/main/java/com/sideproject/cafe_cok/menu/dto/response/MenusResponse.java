package com.sideproject.cafe_cok.menu.dto.response;

import com.sideproject.cafe_cok.cafe.dto.response.CafeTopResponse;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
public class MenusResponse extends RepresentationModel<CafeTopResponse> {

    private List<MenuImageUrlDto> menus;

    public MenusResponse(final List<MenuImageUrlDto> menus) {
        this.menus = menus;
    }
}
