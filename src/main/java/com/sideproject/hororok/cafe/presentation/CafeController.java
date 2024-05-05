package com.sideproject.hororok.cafe.presentation;

import com.sideproject.hororok.auth.application.AuthService;
import com.sideproject.hororok.auth.application.AuthTokenCreator;
import com.sideproject.hororok.auth.application.TokenProvider;
import com.sideproject.hororok.auth.exception.EmptyAuthorizationHeaderException;
import com.sideproject.hororok.auth.exception.InvalidTokenException;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipalArgumentResolver;
import com.sideproject.hororok.auth.presentation.AuthorizationExtractor;
import com.sideproject.hororok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.hororok.cafe.dto.response.*;
import com.sideproject.hororok.cafe.application.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static com.sideproject.hororok.utils.Constants.NO_MEMBER_ID;


@RestController
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
@Tag(name = "Cafe", description = "카페 관련 API")
public class CafeController {

    private final CafeService cafeService;
    private final AuthService authService;

    @GetMapping("/{cafeId}/top")
    @Operation(summary = "해당하는 카페의 상세 정보 상단")
    public ResponseEntity<CafeDetailTopResponse> detailTop(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId, HttpServletRequest servletRequest){

        Long memberId = getMemberId(servletRequest);
        CafeDetailTopResponse response = cafeService.detailTop(cafeId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/basicInfo")
    @Operation(summary = "카페 상세 정보의 기본 정보 탭")
    public ResponseEntity<CafeDetailBasicInfoResponse> detailBasicInfo(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId) {

        CafeDetailBasicInfoResponse response = cafeService.detailBasicInfo(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/menus")
    @Operation(summary = "카페 상세 정보의 메뉴 탭")
    public ResponseEntity<CafeDetailMenuResponse> detailMenus(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId) {

        CafeDetailMenuResponse response = cafeService.detailMenus(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/images")
    @Operation(summary = "카페 상세 정보의 사진 탭(페이징)")
    public ResponseEntity<CafeDetailImagePageResponse> detailImages(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId,
            @RequestParam(required = false) Long cursor) {

        CafeDetailImagePageResponse response = cafeService.detailImages(cafeId, cursor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/images/all")
    @Operation(summary = "카페 상세 정보의 사진의 전체 리스트")
    public ResponseEntity<CafeDetailImageAllResponse> detailImagesAll(@Parameter(description = "카페의 ID") @PathVariable Long cafeId) {

        CafeDetailImageAllResponse response = cafeService.detailImagesAll(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/reviews")
    @Operation(summary = "카페 상세 정보의 리뷰 탭(페이징)")
    public ResponseEntity<CafeDetailReviewPageResponse> detailReviews(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId,
            @RequestParam(required = false) Long cursor) {

        CafeDetailReviewPageResponse response = cafeService.detailReviews(cafeId, cursor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/reviews/all")
    @Operation(summary = "카페 상세 정보의 리뷰 탭(전체 리스트)")
    public ResponseEntity<CafeDetailReviewAllResponse> detailReviewsAll(
            @Parameter(description = "카페의 ID") @PathVariable Long cafeId) {

        CafeDetailReviewAllResponse response = cafeService.detailReviewsAll(cafeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/again")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    public ResponseEntity<CafeFindAgainResponse> findAgain(
            @Parameter(description = "위도 좌표") @RequestParam BigDecimal latitude,
            @Parameter(description = "경도 좌표") @RequestParam BigDecimal longitude,
            HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeFindAgainResponse response = cafeService.findCafeByAgain(latitude, longitude, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/bar")
    @Operation(summary = "검색창에 검색을 했을 때 동작하는 기능")
    @ApiResponse(
            responseCode = "200",
            description =
                    "검색창 검색 성공\n\n" +
                    "찾는 카페가 존재하는 경우: Cafe의 상세 정보 전달, exist = true\n\n" +
                    "찾는 카페가 존재하지 않는 경우: 근처 카페의 리스트 cafes(근처에 카페가 있는 경우), 카테고리와 키워드 정보, exist=false",
            content = @Content(schema = @Schema(implementation = CafeFindBarResponse.class))
    )
    
    public ResponseEntity<CafeFindBarResponse> findBar(
            @Parameter(description = "위도 좌표") @RequestParam BigDecimal latitude,
            @Parameter(description = "경도 좌표") @RequestParam BigDecimal longitude,
            HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeFindBarResponse response = cafeService.findCafeByBar(latitude, longitude, memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find/keyword")
    @Operation(summary = "선택한 키워드와 현재 위치를 기준으로 검색")
    
    public ResponseEntity<CafeFindCategoryResponse> findKeyword(
            @RequestBody CafeFindCategoryRequest request, HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafeFindCategoryResponse response = cafeService.findCafeByKeyword(request, memberId);
        return ResponseEntity.ok(response);
    }

    private Long getMemberId(final HttpServletRequest request) {

        try {
            String accessToken = AuthorizationExtractor.extract(request);
            Long memberId = authService.extractMemberId(accessToken);
            return memberId;
        } catch (final InvalidTokenException | EmptyAuthorizationHeaderException  e) {
            return null;
        }
    }
}
