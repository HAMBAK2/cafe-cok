package com.sideproject.cafe_cok.review.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.review.dto.response.ReviewsResponse;
import com.sideproject.cafe_cok.review.dto.request.ReviewEditRequest;
import com.sideproject.cafe_cok.review.dto.response.ReviewSaveResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewIdResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewResponse;
import com.sideproject.cafe_cok.review.application.ReviewService;
import com.sideproject.cafe_cok.review.dto.request.ReviewSaveRequest;
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
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "리뷰 목록 조회")
    public ResponseEntity<ReviewsResponse> reviews(@AuthenticationPrincipal LoginMember loginMember) {

        ReviewsResponse response = reviewService.getReviews(loginMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "리뷰 저장")
    public ResponseEntity<ReviewSaveResponse> save(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestPart ReviewSaveRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files){

        ReviewSaveResponse response = reviewService.save(request, loginMember, files);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "reviewId에 해당하는 리뷰 삭제")
    public ResponseEntity<ReviewIdResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                   @PathVariable Long reviewId) {

        ReviewIdResponse response = reviewService.delete(reviewId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "reviewId에 해당하는 리뷰 조회")
    public ResponseEntity<ReviewResponse> detail(@AuthenticationPrincipal LoginMember loginMember,
                                                 @PathVariable Long reviewId) {

        ReviewResponse response = reviewService.detail(reviewId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{reviewId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "reviewId에 해당하는 리뷰 수정")
    public ResponseEntity<ReviewIdResponse> edit(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long reviewId,
            @RequestPart ReviewEditRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        ReviewIdResponse response = reviewService.edit(request, files, reviewId);
        return ResponseEntity.ok(response);
    }
}
