package com.sideproject.cafe_cok.menu.presentation;

import com.sideproject.cafe_cok.menu.dto.response.MenuListResponse;
import com.sideproject.cafe_cok.menu.application.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "메뉴 API")
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{cafeId}")
    @Operation(summary = "cafeId에 해당하는 메뉴 조회")
    public ResponseEntity<MenuListResponse> findByCafeId(@PathVariable Long cafeId) {

        MenuListResponse response = menuService.findByCafeId(cafeId);
        return ResponseEntity.ok(response);
    }
}
