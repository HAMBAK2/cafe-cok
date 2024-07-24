package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.application.AdminService;
import com.sideproject.cafe_cok.admin.dto.AdminOperationHourDto;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeUpdateRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminRestoreMemberResponse;
import com.sideproject.cafe_cok.admin.dto.response.AdminSuccessAndRedirectResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminService adminService;

    @Value("${oauth.kakao.client-id}")
    private String kakaoApiKey;
    @PutMapping("/cafe/{id}")
    public ResponseEntity<AdminSuccessAndRedirectResponse> updateCafe(@PathVariable Long id,
                                                                      @RequestBody AdminCafeUpdateRequest request) {

        AdminSuccessAndRedirectResponse response = adminService.updateCafe(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<String> menuDelete(@PathVariable Long id) {
        boolean success = adminService.menuDelete(id);
        if(success) return ResponseEntity.ok("메뉴가 성공적으로 삭제되었습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제에 실패했습니다.");
    }

    @PostMapping("/cafe")
    public ResponseEntity<AdminSuccessAndRedirectResponse> saveCafe(@RequestBody AdminCafeSaveRequest request) {
        List<AdminOperationHourDto> hours = request.getHours();
        for (AdminOperationHourDto hour : hours) {
            System.out.println(hour.getDay());
            System.out.println(hour.getStartHour());
            System.out.println(hour.getEndHour());
        }
        AdminSuccessAndRedirectResponse response = adminService.saveCafe(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/member/restore")
    @Operation(summary = "탈퇴한 회원을 복구하는 기능")
    public ResponseEntity<AdminRestoreMemberResponse> restoreMember(@RequestParam("memberId") Long memberId) {

        AdminRestoreMemberResponse response = adminService.restoreMember(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cafe/exists/{kakaoId}")
    public ResponseEntity<Boolean> checkCafeExists(@PathVariable Long kakaoId) {
        boolean exists = adminService.cafeExistsByKakaoId(kakaoId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/app-key")
    public String getAppKey() {
        return kakaoApiKey;
    }
}
