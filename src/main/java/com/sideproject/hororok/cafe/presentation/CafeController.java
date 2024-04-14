package com.sideproject.hororok.cafe.presentation;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.cond.CafeCategorySearchCond;
import com.sideproject.hororok.cafe.cond.CafeSearchCond;
import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.service.CafePlanService;
import com.sideproject.hororok.cafe.service.CafeService;
import com.sideproject.hororok.category.dto.CategoryKeywords;
import com.sideproject.hororok.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    private final CafePlanService cafePlanService;
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "홈 화면에 보여줄 정보를 제공")
    @LogTrace
    public ResponseEntity<CategoryKeywords> home() {

        return ResponseEntity.ok(categoryService.findAllCategoryAndKeyword());
    }

    @GetMapping("/{cafeId}")
    @Operation(summary = "해당하는 카페의 상세 정보를 보여주는 기능")
    @LogTrace
    public ResponseEntity<CafeDetailDto> detail(
            @Parameter(description = "카페의 ID")
            @PathVariable Long cafeId){
        return ResponseEntity.ok(cafeService.findCafeDetail(cafeId));
    }


    @GetMapping("/find/re")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    @Parameter(description = "현재 위치의 경도 위도 값")
    @LogTrace
    public ResponseEntity<CafeReSearchDto> searchRe(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        return ResponseEntity.ok(cafeService.findWithinRadius(CafeSearchCond.of(latitude, longitude)));
    }

    @GetMapping("/find/bar")
    @Operation(summary = "검색창에 검색을 했을 때 동작하는 기능")
    @Parameter(description = "현재 위치의 경도 위도 값")
    @ApiResponse(description = "선택한 카페의 상세 정보를 전달, 카페가 존재하지 않는 경우 cafes, keywordsByCategory 정보 존재 나머지 X, \n카페가 존재하는 경우 반대 (exist 값은 항상 존재)")
    @LogTrace
    public ResponseEntity<CafeBarSearchDto> searchBar(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        return ResponseEntity.ok(cafeService.barSearch(CafeSearchCond.of(latitude, longitude)));
    }

    @PostMapping("/find/category")
    @Operation(summary = "선택한 키워드와 현재 위치를 기준으로 검색")
    @Parameter(description = "현재 위치의 경도 위도 값, 선태한 키워드")
    @LogTrace
    public ResponseEntity<CafeCategorySearchDto> searchCategory(@RequestBody CafeCategorySearchCond searchCond) {

        return ResponseEntity.ok(cafeService.categorySearch(searchCond));
    }

    @PostMapping("/plans")
    @Operation(summary = "계획하기를 통해 선택한 항목에 대한 결과를 제공")
    @Parameter(description = "방문위치(좌표), 몇분거리, 방문일자(요일 하나, 시간은 범위), 키워드 5개")
    @LogTrace
    public ResponseEntity<CreatePlanDto> createPlan( @RequestBody CreatePlanSearchCond searchCond
            ) {

        return ResponseEntity.ok(cafePlanService.createPlans(searchCond));
    }
}
