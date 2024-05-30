package com.sideproject.cafe_cok.review.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.review.dto.request.ReviewEditRequest;
import com.sideproject.cafe_cok.review.dto.response.ReviewCreateResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewDetailResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewEditResponse;
import com.sideproject.cafe_cok.review.application.ReviewService;
import com.sideproject.cafe_cok.review.dto.request.ReviewCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ReviewCreateResponse> createReview(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestPart ReviewCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files){

        ReviewCreateResponse response = reviewService.createReview(request, loginMember, files);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/review/{reviewId}/delete")
    @Operation(summary = "리뷰 삭제 기능")
    public ResponseEntity<ReviewDeleteResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                       @PathVariable Long reviewId) {

        ReviewDeleteResponse response = reviewService.delete(reviewId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 상세 정보를 반환하는 기능")
    public ResponseEntity<ReviewDetailResponse> detail(@AuthenticationPrincipal LoginMember loginMember,
                                                       @PathVariable Long reviewId) {

        ReviewDetailResponse response = reviewService.detail(reviewId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/review/{reviewId}/edit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "리뷰 수정 기능")
    public ResponseEntity<ReviewEditResponse> edit(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long reviewId,
            @RequestPart ReviewEditRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        ReviewEditResponse response = reviewService.edit(request, files, reviewId);
        return ResponseEntity.ok(response);
    }

}
