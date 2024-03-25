package com.sideproject.hororok.cafe.controller;


import com.sideproject.hororok.cafe.cond.CafeSearchCond;
import com.sideproject.hororok.cafe.dto.CafeDetailDto;
import com.sideproject.hororok.cafe.dto.CafeReSearchDto;
import com.sideproject.hororok.cafe.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


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




    @GetMapping("/search")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    @Parameter(description = "현재 위치의 경도 위도 값")
    public ResponseEntity<CafeReSearchDto> search(@RequestBody CafeSearchCond cafeSearchCond) {

        return ResponseEntity.ok(cafeService.findWithinRadius(cafeSearchCond));
    }
}
