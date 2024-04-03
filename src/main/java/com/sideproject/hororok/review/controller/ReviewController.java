package com.sideproject.hororok.review.controller;


import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.review.dto.ReviewInfo;
import com.sideproject.hororok.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/add")
    @Operation(summary = "리뷰 작성 기능")
    @Parameter(description = "ReviewInfo: 사진을 제외한 Review의 정보를 담은 JSON, files: Multipart 파일의 List")
    @LogTrace
    public ResponseEntity<Void> addReview(
            @RequestPart ReviewInfo reviewInfo,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
        ) throws IOException {


        //임시로 임의의 유저 값을 사용! 로그인 개발 이후에는 헤더에 있는 유저 정보를 이용해 해당 유저의 값을 사용
        Long userId = 1L;
        reviewService.addReview(reviewInfo, userId, files);

        return ResponseEntity.ok().build();

    }



}
