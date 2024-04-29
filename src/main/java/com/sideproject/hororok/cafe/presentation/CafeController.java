package com.sideproject.hororok.cafe.presentation;

import com.sideproject.hororok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.hororok.cafe.dto.response.*;
import com.sideproject.hororok.cafe.application.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
@Tag(name = "Cafe", description = "카페 관련 API")
public class CafeController {

    private final CafeService cafeService;

    @GetMapping
    @Operation(summary = "홈 화면에 보여줄 정보를 제공")
    
    public ResponseEntity<CafeHomeResponse> home() {

        CafeHomeResponse response = cafeService.home();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/top")
    @Operation(summary = "해당하는 카페의 상세 정보 상단")
    public ResponseEntity<CafeDetailTopResponse> detailTop(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId){

        CafeDetailTopResponse response = cafeService.detailTop(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/basicInfo")
    @Operation(summary = "카페 상세 정보의 기본 정보 탭")
    public ResponseEntity<CafeDetailBasicInfoResponse> detailBasicInfo(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId) {

        CafeDetailBasicInfoResponse response = cafeService.detailBasicInfo(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/menus")
    @Operation(summary = "카페 상세 정보의 메뉴 탭")
    public ResponseEntity<CafeDetailMenuResponse> detailMenus(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId) {

        CafeDetailMenuResponse response = cafeService.detailMenus(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/images")
    @Operation(summary = "카페 상세 정보의 사진 탭")
    public ResponseEntity<CafeDetailImageResponse> detailImage(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId) {

        CafeDetailImageResponse response = cafeService.detailImages(cafeId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/find/again")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    
    public ResponseEntity<CafeFindAgainResponse> findAgain(
            @Parameter(description = "위도 좌표") @RequestParam BigDecimal latitude,
            @Parameter(description = "경도 좌표") @RequestParam BigDecimal longitude) {

        CafeFindAgainResponse response = cafeService.findCafeByAgain(latitude, longitude);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/bar")
    @Operation(summary = "검색창에 검색을 했을 때 동작하는 기능")
    @ApiResponse(
            responseCode = "200",
            description =
                    "검색창 검색 성공\n\n" +
                    "찾는 카페가 존재하는 경우: Cafe의 상세 정보 전달, exist = true\n\n" +
                    "찾는 카페가 존재하지 않는 경우: 근처 카페의 리스트 cafes(근처에 카페가 있는 경우), 카테고리와 키워드 정보, exist=false",
            content = @Content(schema = @Schema(implementation = CafeFindBarResponse.class))
    )
    
    public ResponseEntity<CafeFindBarResponse> findBar(
            @Parameter(description = "위도 좌표") @RequestParam BigDecimal latitude,
            @Parameter(description = "경도 좌표") @RequestParam BigDecimal longitude) {

        CafeFindBarResponse response = cafeService.findCafeByBar(latitude, longitude);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find/keyword")
    @Operation(summary = "선택한 키워드와 현재 위치를 기준으로 검색")
    
    public ResponseEntity<CafeFindCategoryResponse> findKeyword(
            @RequestBody CafeFindCategoryRequest request) {

        CafeFindCategoryResponse response = cafeService.findCafeByKeyword(request);
        return ResponseEntity.ok(response);
    }


}
