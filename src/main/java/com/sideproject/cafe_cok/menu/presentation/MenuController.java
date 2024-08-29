package com.sideproject.cafe_cok.menu.presentation;

import com.sideproject.cafe_cok.menu.dto.response.MenuListResponse;
import com.sideproject.cafe_cok.menu.application.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "메뉴 API")
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/cafe/{cafeId}")
    @Operation(summary = "cafeId에 해당하는 메뉴 조회")
    public ResponseEntity<MenuListResponse> findByCafeId(@PathVariable Long cafeId) {

        MenuListResponse response = menuService.findByCafeId(cafeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> delete(@PathVariable Long menuId) {
        boolean success = menuService.delete(menuId);
        if(success) return ResponseEntity.ok("메뉴가 성공적으로 삭제되었습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제에 실패했습니다.");
    }

}
