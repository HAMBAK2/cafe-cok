package com.sideproject.cafe_cok.review.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.bookmark.presentation.BookmarkController;
import com.sideproject.cafe_cok.cafe.presentation.CafeController;
import com.sideproject.cafe_cok.review.dto.response.ReviewsResponse;
import com.sideproject.cafe_cok.review.dto.request.ReviewEditRequest;
import com.sideproject.cafe_cok.review.dto.response.ReviewSaveResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewIdResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewResponse;
import com.sideproject.cafe_cok.review.application.ReviewService;
import com.sideproject.cafe_cok.review.dto.request.ReviewSaveRequest;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "reviews", description = "리뷰 관련 API")
@ApiResponse(responseCode = "200", description = "성공")
public class ReviewController {

    private final ReviewService reviewService;
    private final HttpHeadersUtil httpHeadersUtil;

    @GetMapping
    @Operation(summary = "리뷰 목록 조회")
    public ResponseEntity<ReviewsResponse> findList(@AuthenticationPrincipal LoginMember loginMember) {

        ReviewsResponse response = reviewService.findList(loginMember);
        response.add(linkTo(methodOn(ReviewController.class).findList(loginMember)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).detail(null, null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).delete(null, null)).withRel("delete").withType("DELETE"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("reviews/findList");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


    @Operation(summary = "리뷰 저장")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewSaveResponse> save(@AuthenticationPrincipal LoginMember loginMember,
                                                   @RequestPart ReviewSaveRequest request,
                                                   @RequestPart(value = "files", required = false) List<MultipartFile> files){

        ReviewSaveResponse response = reviewService.save(request, loginMember, files);
        response.add(linkTo(methodOn(ReviewController.class).save(loginMember, request, files)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(CafeController.class).findTop(null, null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findBasic(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findMenus(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findImages(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findReviews(null)).withRel("detail").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("reviews/save");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "reviewId에 해당하는 리뷰 삭제")
    @Parameter(name = "reviewId", description = "삭제하려는 리뷰 ID", example = "1")
    public ResponseEntity<ReviewIdResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                   @PathVariable Long reviewId) {

        ReviewIdResponse response = reviewService.delete(reviewId);
        response.add(linkTo(methodOn(ReviewController.class).delete(loginMember, reviewId)).withSelfRel().withType("DELETE"))
                .add(linkTo(methodOn(ReviewController.class).findList(null)).withRel("list").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("reviews/delete");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "reviewId에 해당하는 리뷰 조회")
    @Parameter(name = "reviewId", description = "조회하려는 리뷰 ID", example = "1")
    public ResponseEntity<ReviewResponse> detail(@AuthenticationPrincipal LoginMember loginMember,
                                                 @PathVariable Long reviewId) {

        ReviewResponse response = reviewService.find(reviewId);
        response.add(linkTo(methodOn(ReviewController.class).detail(loginMember, reviewId)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).update(null, null, null, null)).withRel("update").withType("PATCH"))
                .add(linkTo(methodOn(ReviewController.class).delete(null, null)).withRel("delete").withType("DELETE"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("reviews/detail");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PatchMapping(
            value = "/{reviewId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "reviewId에 해당하는 리뷰 수정")
    @Parameter(name = "reviewId", description = "수정하려는 리뷰 ID", example = "1")
    public ResponseEntity<ReviewIdResponse> update(@AuthenticationPrincipal LoginMember loginMember,
                                                   @PathVariable Long reviewId,
                                                   @RequestPart ReviewEditRequest request,
                                                   @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        ReviewIdResponse response = reviewService.update(request, files, reviewId);
        response.add(linkTo(methodOn(ReviewController.class).update(null, null, null, null)).withSelfRel().withType("PATCH"))
                .add(linkTo(methodOn(ReviewController.class).detail(loginMember, reviewId)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).delete(null, null)).withRel("delete").withType("DELETE"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("reviews/update");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
