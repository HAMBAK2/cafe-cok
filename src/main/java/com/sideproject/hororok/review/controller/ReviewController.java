package com.sideproject.hororok.review.controller;


import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.review.dto.SaveReviewDto;
import com.sideproject.hororok.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/add")
    @LogTrace
    public ResponseEntity<Void> addReview(
            @RequestPart SaveReviewDto saveReviewDto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
        ) throws IOException {


        //임시로 임의의 유저 값을 사용! 로그인 개발 이후에는 헤더에 있는 유저 정보를 이용해 해당 유저의 값을 사용
        Long userId = 1L;
        reviewService.addReview(saveReviewDto, userId, files);

        return ResponseEntity.ok().build();

    }



}
