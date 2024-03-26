package com.sideproject.hororok.cafe.controller;

import com.sideproject.hororok.cafe.cond.CafeCategorySearchCond;
import com.sideproject.hororok.cafe.cond.CafeSearchCond;
import com.sideproject.hororok.cafe.dto.CafeBarSearchDto;
import com.sideproject.hororok.cafe.dto.CafeCategorySearchDto;
import com.sideproject.hororok.cafe.dto.CafeDetailDto;
import com.sideproject.hororok.cafe.dto.CafeReSearchDto;
import com.sideproject.hororok.cafe.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/detail/{cafeId}")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    @Parameter(description = "카페의 ID")
    public ResponseEntity<CafeDetailDto> detail(@PathVariable Long cafeId){

        return ResponseEntity.ok(cafeService.findCafeDetail(cafeId));
    }


    @GetMapping("/search/re")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    @Parameter(description = "현재 위치의 경도 위도 값")
    public ResponseEntity<CafeReSearchDto> searchRe(@RequestBody CafeSearchCond cafeSearchCond) {

        return ResponseEntity.ok(cafeService.findWithinRadius(cafeSearchCond));
    }

    @GetMapping("/search/bar")
    @Operation(summary = "검색창에 검색을 했을 때 동작하는 기능")
    @Parameter(description = "현재 위치의 경도 위도 값")
    @ApiResponse(description = "선택한 카페의 상세 정보를 전달, 카페가 존재하지 않는 경우 cafes, keywordsByCategory 정보 존재 나머지 X, \n카페가 존재하는 경우 반대 (exist 값은 항상 존재)")
    public ResponseEntity<CafeBarSearchDto> searchBar(@RequestBody CafeSearchCond cafeSearchCond) {

        return ResponseEntity.ok(cafeService.barSearch(cafeSearchCond));
    }

    @GetMapping("/search/category")
    @Operation(summary = "선택한 키워드와 현재 위치를 기준으로 검색")
    @Parameter(description = "현재 위치의 경도 위도 값, 선태한 키워드")
    public ResponseEntity<CafeCategorySearchDto> searchCategory(@RequestBody CafeCategorySearchCond cafeCategorySearchCond) {

        return ResponseEntity.ok(cafeService.categorySearch(cafeCategorySearchCond));
    }
}
