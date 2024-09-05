package com.sideproject.cafe_cok.menu.presentation;

import com.sideproject.cafe_cok.menu.application.MenuService;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Tag(name = "menus", description = "메뉴 API")
public class MenuController {

    private final MenuService menuService;
    private final HttpHeadersUtil httpHeadersUtil;

    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> delete(@PathVariable Long menuId) {
        menuService.delete(menuId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("menus/delete");
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

}
