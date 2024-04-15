package com.sideproject.hororok.review.presentation;


import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.review.application.ReviewService;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/review/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "리뷰 작성 기능")
    @LogTrace
    public ResponseEntity<Void> createReview(
            @AuthenticationPrincipal LoginMember loginMember,
            @Parameter(description = "사진을 제외한 Review의 정보를 담은 객체")
            @RequestPart ReviewCreateRequest request,
            @Parameter(description = "사용자가 업로드한 이미지 파일들")
            @RequestPart(value = "files", required = false) List<MultipartFile> files
        ) throws IOException {

        reviewService.createReview(request, loginMember.getId(), files);
        return ResponseEntity.noContent().build();

    }



}
