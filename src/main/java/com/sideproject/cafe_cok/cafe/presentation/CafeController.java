package com.sideproject.cafe_cok.cafe.presentation;

import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.exception.EmptyAuthorizationHeaderException;
import com.sideproject.cafe_cok.auth.exception.InvalidTokenException;
import com.sideproject.cafe_cok.auth.presentation.AuthorizationExtractor;
import com.sideproject.cafe_cok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.cafe.application.CafeService;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
@Tag(name = "Cafe", description = "카페 관련 API")
public class CafeController {

    private final CafeService cafeService;
    private final AuthService authService;

    @GetMapping("/{cafeId}/top")
    @Operation(summary = "해당하는 카페의 상세 정보 상단")
    public ResponseEntity<CafeDetailTopResponse> detailTop(@PathVariable Long cafeId,
                                                           HttpServletRequest servletRequest){

        Long memberId = getMemberId(servletRequest);
        CafeDetailTopResponse response = cafeService.detailTop(cafeId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/basicInfo")
    @Operation(summary = "카페 상세 정보의 기본 정보 탭")
    public ResponseEntity<CafeDetailBasicInfoResponse> detailBasicInfo(@PathVariable Long cafeId) {

        CafeDetailBasicInfoResponse response = cafeService.detailBasicInfo(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/menus")
    @Operation(summary = "카페 상세 정보의 메뉴 탭")
    public ResponseEntity<CafeDetailMenuResponse> detailMenus(@PathVariable Long cafeId) {

        CafeDetailMenuResponse response = cafeService.detailMenus(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/images")
    @Operation(summary = "카페 상세 정보의 사진 탭(페이징)")
    public ResponseEntity<CafeDetailImagePageResponse> detailImages(@PathVariable Long cafeId,
                                                                    @RequestParam(required = false) Long cursor) {

        CafeDetailImagePageResponse response = cafeService.detailImages(cafeId, cursor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/images/all")
    @Operation(summary = "카페 상세 정보의 사진의 전체 리스트")
    public ResponseEntity<CafeDetailImageAllResponse> detailImagesAll(@PathVariable Long cafeId) {

        CafeDetailImageAllResponse response = cafeService.detailImagesAll(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/reviews")
    @Operation(summary = "카페 상세 정보의 리뷰 탭(페이징)")
    public ResponseEntity<CafeDetailReviewPageResponse> detailReviews(@PathVariable Long cafeId,
                                                                      @RequestParam(required = false) Long cursor) {

        CafeDetailReviewPageResponse response = cafeService.detailReviews(cafeId, cursor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/reviews/all")
    @Operation(summary = "카페 상세 정보의 리뷰 탭(전체 리스트)")
    public ResponseEntity<CafeDetailReviewAllResponse> detailReviewsAll(@PathVariable Long cafeId) {

        CafeDetailReviewAllResponse response = cafeService.detailReviewsAll(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/again")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    public ResponseEntity<CafeSearchResponse> findAgain(@RequestParam BigDecimal latitude,
                                                        @RequestParam BigDecimal longitude,
                                                        HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeSearchResponse response = cafeService.getCafeListByAgain(latitude, longitude, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/bar")
    @Operation(summary = "검색창에 검색을 했을 때 동작하는 기능")
    public ResponseEntity<CafeSearchResponse> findBar(@RequestParam BigDecimal latitude,
                                                      @RequestParam BigDecimal longitude,
                                                      HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeSearchResponse response = cafeService.findCafeByBar(latitude, longitude, memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find/keyword")
    @Operation(summary = "선택한 키워드와 현재 위치를 기준으로 검색")
    public ResponseEntity<CafeSearchResponse> findKeyword(@RequestBody CafeFindCategoryRequest request,
                                                          HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeSearchResponse response = cafeService.findCafeByKeyword(request, memberId);
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
