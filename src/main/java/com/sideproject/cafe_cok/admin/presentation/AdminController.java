package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.application.AdminService;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveTestRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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


    @PostMapping(value = "/cafe/save/test",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "카페를 저장하는 기능 테스트")
    public ResponseEntity<AdminCafeSaveResponse> saveCafeTest(
            @RequestPart AdminCafeSaveTestRequest request,
            @RequestPart(value = "mainImage") MultipartFile mainImage,
            @RequestPart(value = "otherImages", required = false) List<MultipartFile> otherImages) {

        AdminCafeSaveResponse response = adminService.saveCafeTest(request, mainImage, otherImages);
        return ResponseEntity.ok(response);
    }
}
