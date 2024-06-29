package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.application.AdminService;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeExistResponse;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeFindResponse;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import com.sideproject.cafe_cok.admin.dto.response.AdminRestoreMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 페이지 관련 API")
public class AdminController {

    private final AdminService adminService;

    @PostMapping(value = "/cafe/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "카페를 저장하는 기능")
    public ResponseEntity<AdminCafeSaveResponse> saveCafe(
            @RequestPart AdminCafeSaveRequest request,
            @RequestPart(value = "mainImage") MultipartFile mainImage,
            @RequestPart(value = "otherImages", required = false) List<MultipartFile> otherImages) {

        AdminCafeSaveResponse response = adminService.saveCafe(request, mainImage, otherImages);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/member/restore")
    @Operation(summary = "탈퇴한 회원을 복구하는 기능")
    public ResponseEntity<AdminRestoreMemberResponse> restoreMember(@RequestParam("memberId") Long memberId) {

        AdminRestoreMemberResponse response = adminService.restoreMember(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/cafe/exist")
    @Operation(summary = "저장하려는 카페가 존재하는지 확인하는 기능")
    public ResponseEntity<AdminCafeExistResponse> checkCafeExist(@RequestParam BigDecimal mapx,
                                                                 @RequestParam BigDecimal mapy){

        AdminCafeExistResponse response = adminService.checkCafeExist(mapx, mapy);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/cafe")
    @Operation(summary = "검색한 카페가 존재하면 카페의 정보를 반환하는 기능")
    public ResponseEntity<AdminCafeFindResponse> findCafe(@RequestParam BigDecimal mapx,
                                                           @RequestParam BigDecimal mapy){

        AdminCafeFindResponse response = adminService.findCafe(mapx, mapy);
        return ResponseEntity.ok(response);
    }
}
