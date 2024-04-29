package com.sideproject.hororok.review.presentation;


import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.review.application.ReviewService;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import com.sideproject.hororok.review.dto.request.ReviewEditRequest;
import com.sideproject.hororok.review.dto.response.ReviewCreateResponse;
import com.sideproject.hororok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.hororok.review.dto.response.ReviewDetailResponse;
import com.sideproject.hororok.review.dto.response.ReviewEditPatchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
            @Parameter(description = "사진을 제외한 Review의 정보를 담은 객체")
            @RequestPart ReviewCreateRequest request,
            @Parameter(description = "사용자가 업로드한 이미지 파일들")
            @RequestPart(value = "files", required = false) List<MultipartFile> files){

        ReviewCreateResponse response = reviewService.createReview(request, loginMember, files);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/review/{reviewId}/delete")
    @Operation(summary = "리뷰 삭제 기능")
    public ResponseEntity<ReviewDeleteResponse> delete(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long reviewId) {

        ReviewDeleteResponse response = reviewService.delete(reviewId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 상세 정보를 반환하는 기능")
    public ResponseEntity<ReviewDetailResponse> detail(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long reviewId) {

        ReviewDetailResponse response = reviewService.detail(reviewId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/review/{reviewId}/edit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "리뷰 수정 후 수정을 적용하기 위한 요청")
    public ResponseEntity<ReviewEditPatchResponse> editPost(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long reviewId,
            @RequestPart ReviewEditRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        ReviewEditPatchResponse response = reviewService.editPatch(request, files, reviewId);
        return ResponseEntity.ok(response);
    }

}
