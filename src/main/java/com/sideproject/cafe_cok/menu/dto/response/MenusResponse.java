package com.sideproject.cafe_cok.menu.dto.response;

import com.sideproject.cafe_cok.menu.dto.MenuImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 메뉴 조회 응답")
public class MenusResponse extends RepresentationModel<MenusResponse> {

    @Schema(description = "카페 메뉴 리스트")
    private List<MenuImageDto> menus;

    public MenusResponse(final List<MenuImageDto> menus) {
        this.menus = menus;
    }
}
