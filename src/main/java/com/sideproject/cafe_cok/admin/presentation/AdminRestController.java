package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.application.AdminService2;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeUpdateRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminSuccessAndRedirectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminService2 adminService2;

    @PostMapping("/cafes/{id}")
    public ResponseEntity<AdminSuccessAndRedirectResponse> updateCafe(@PathVariable Long id,
                                                                      @RequestBody AdminCafeUpdateRequest request) {

        AdminSuccessAndRedirectResponse response = adminService2.updateCafe(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/menu/delete/{id}")
    public ResponseEntity<String> menuDelete(@PathVariable Long id) {
        boolean success = adminService2.menuDelete(id);
        if(success) return ResponseEntity.ok("메뉴가 성공적으로 삭제되었습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제에 실패했습니다.");
    }

}
