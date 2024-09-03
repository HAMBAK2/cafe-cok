package com.sideproject.cafe_cok.cafe.presentation;

import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeUpdateRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminSuccessAndRedirectResponse;
import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.exception.EmptyAuthorizationHeaderException;
import com.sideproject.cafe_cok.auth.exception.InvalidTokenException;
import com.sideproject.cafe_cok.auth.presentation.AuthorizationExtractor;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.cafe.application.CafeService;
import com.sideproject.cafe_cok.image.dto.response.ImagesResponse;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import com.sideproject.cafe_cok.menu.dto.response.MenusResponse;
import com.sideproject.cafe_cok.review.application.ReviewService;
import com.sideproject.cafe_cok.review.dto.response.CafeReviewsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/cafes")
@RequiredArgsConstructor
@Tag(name = "Cafe", description = "카페 API")
public class CafeController {

    private final CafeService cafeService;
    private final ReviewService reviewService;
    private final AuthService authService;

    @GetMapping("/{cafeId}/top")
    @Operation(summary = "cafeId에 해당하는 카페의 상단 정보 조회")
    public ResponseEntity<CafeDetailTopResponse> detailTop(@PathVariable Long cafeId,
                                                           HttpServletRequest servletRequest){

        Long memberId = getMemberId(servletRequest);
        CafeDetailTopResponse response = cafeService.detailTop(cafeId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/basic")
    @Operation(summary = "cafeId에 해당하는 카페의 기본 정보 조회")
    public ResponseEntity<CafeDetailBasicInfoResponse> detailBasicInfo(@PathVariable Long cafeId) {

        CafeDetailBasicInfoResponse response = cafeService.detailBasicInfo(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "좌표 기반 카페 조회")
    public ResponseEntity<CafeListResponse> findByLatitudeAndLongitude(@RequestParam BigDecimal latitude,
                                                                       @RequestParam BigDecimal longitude,
                                                                       HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeListResponse response = cafeService.getNearestCafes(latitude, longitude, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/keyword")
    @Operation(summary = "좌표와 키워드 기반 카페 조회")
    public ResponseEntity<CafeListResponse> findByLatitudeAndLongitudeAndKeyword(@RequestParam BigDecimal latitude,
                                                                                 @RequestParam BigDecimal longitude,
                                                                                 @RequestParam List<String> keywords,
                                                                                 HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeListResponse response = cafeService.findCafeByKeyword(latitude, longitude, keywords, memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "카페 저장")
    public ResponseEntity<AdminSuccessAndRedirectResponse> save(@RequestBody AdminCafeSaveRequest request) {
        AdminSuccessAndRedirectResponse response = cafeService.save(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cafeId}")
    @Operation(summary = "cafeId에 해당하는 카페 수정")
    public ResponseEntity<AdminSuccessAndRedirectResponse> update(@PathVariable Long cafeId,
                                                                      @RequestBody AdminCafeUpdateRequest request) {

        AdminSuccessAndRedirectResponse response = cafeService.update(cafeId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{kakaoId}")
    @Operation(summary = "kakao map의 장소 ID에 해당하는 카페 존재여부 조회")
    public ResponseEntity<Boolean> isExistByKakaoId(@PathVariable Long kakaoId) {
        boolean exists = cafeService.isExistByKakaoId(kakaoId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{cafeId}/reviews")
    @Operation(summary = "cafeId에 해당하는 모든 리뷰 조회")
    public ResponseEntity<CafeReviewsResponse> findReviews(@PathVariable Long cafeId) {

        CafeReviewsResponse response = cafeService.findReviews(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/images")
    @Operation(summary = "cafeId에 해당하는 모든 이미지 조회")
    public ResponseEntity<ImagesResponse> findImages(@PathVariable Long cafeId) {

        ImagesResponse response = cafeService.findImages(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/menus")
    @Operation(summary = "cafeId에 해당하는 모든 메뉴 조회")
    public ResponseEntity<MenusResponse> findByCafeId(@PathVariable Long cafeId) {

        MenusResponse response = cafeService.findMenus(cafeId);
        return ResponseEntity.ok(response);
    }

    private Long getMemberId(final HttpServletRequest request) {

        try {
            String accessToken = AuthorizationExtractor.extract(request);
            Long memberId = authService.extractMemberId(accessToken);
            return memberId;
        } catch (final InvalidTokenException | EmptyAuthorizationHeaderException |
                       NoSuchMemberException e) {
            return null;
        }
    }
}
