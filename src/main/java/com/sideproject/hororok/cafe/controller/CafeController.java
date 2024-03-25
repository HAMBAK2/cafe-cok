package com.sideproject.hororok.cafe.controller;


import com.sideproject.hororok.cafe.dto.CafeDetailDto;
import com.sideproject.hororok.cafe.repository.CafeRepository;
import com.sideproject.hororok.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/detail/{cafeId}")
    public ResponseEntity<CafeDetailDto> detail(@PathVariable Long cafeId){

        return ResponseEntity.ok(cafeService.findCafeDetail(cafeId));
    }
}
