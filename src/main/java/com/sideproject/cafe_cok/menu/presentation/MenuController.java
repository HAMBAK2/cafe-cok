package com.sideproject.cafe_cok.menu.presentation;

import com.sideproject.cafe_cok.cafe.presentation.CafeController;
import com.sideproject.cafe_cok.menu.application.MenuService;
import com.sideproject.cafe_cok.menu.dto.response.MenuIdResponse;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Tag(name = "menus", description = "메뉴 API")
@ApiResponse(responseCode = "200", description = "성공")
public class MenuController {

    private final MenuService menuService;
    private final HttpHeadersUtil httpHeadersUtil;

    @DeleteMapping("/{menuId}")
    @Parameter(name = "menuId", description = "삭제하려는 메뉴 ID")
    public ResponseEntity<MenuIdResponse> delete(@PathVariable Long menuId) {
        MenuIdResponse response = menuService.delete(menuId);
        response.add(linkTo(methodOn(MenuController.class).delete(menuId)).withSelfRel().withType("DELETE"))
                .add(linkTo(methodOn(CafeController.class).save(null)).withRel("save").withType("POST"))
                .add(linkTo(methodOn(CafeController.class).update(null, null)).withRel("update").withType("UPDATE"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("menus/delete");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
