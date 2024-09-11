package com.sideproject.cafe_cok.menu.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "메뉴 삭제 응답")
public class MenuIdResponse extends RepresentationModel<MenuIdResponse> {

    @Schema(description = "메뉴 ID", example = "1")
    private Long menuId;

    public MenuIdResponse(Long menuId) {
        this.menuId = menuId;
    }
}
