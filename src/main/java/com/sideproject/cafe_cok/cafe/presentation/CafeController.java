package com.sideproject.cafe_cok.cafe.presentation;

import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.exception.EmptyAuthorizationHeaderException;
import com.sideproject.cafe_cok.auth.exception.InvalidTokenException;
import com.sideproject.cafe_cok.auth.presentation.AuthorizationExtractor;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.cafe.application.CafeService;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import com.sideproject.cafe_cok.menu.dto.response.MenuListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
@Tag(name = "Cafe", description = "카페 API")
public class CafeController {

    private final CafeService cafeService;
    private final AuthService authService;

    @GetMapping("/{cafeId}/top")
    @Operation(summary = "cafeId에 해당하는 카페의 상단 정보 조회")
    public ResponseEntity<CafeDetailTopResponse> detailTop(@PathVariable Long cafeId,
                                                           HttpServletRequest servletRequest){

        Long memberId = getMemberId(servletRequest);
        CafeDetailTopResponse response = cafeService.detailTop(cafeId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cafeId}/basicInfo")
    @Operation(summary = "cafeId에 해당하는 카페의 기본 정보 탭 정보 조회")
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
