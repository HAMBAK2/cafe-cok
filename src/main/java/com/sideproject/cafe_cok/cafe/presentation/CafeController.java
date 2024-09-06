package com.sideproject.cafe_cok.cafe.presentation;

import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeUpdateRequest;
import com.sideproject.cafe_cok.cafe.dto.response.CafeSaveResponse;
import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.exception.EmptyAuthorizationHeaderException;
import com.sideproject.cafe_cok.auth.exception.InvalidTokenException;
import com.sideproject.cafe_cok.auth.presentation.AuthorizationExtractor;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.cafe.application.CafeService;
import com.sideproject.cafe_cok.image.dto.response.ImagesResponse;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import com.sideproject.cafe_cok.menu.dto.response.MenusResponse;
import com.sideproject.cafe_cok.menu.presentation.MenuController;
import com.sideproject.cafe_cok.review.dto.response.CafeReviewsResponse;
import com.sideproject.cafe_cok.review.presentation.ReviewController;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/cafes")
@RequiredArgsConstructor
@Tag(name = "cafes", description = "카페 API")
public class CafeController {

    private final CafeService cafeService;
    private final AuthService authService;
    private final HttpHeadersUtil httpHeadersUtil;

    @GetMapping("/{cafeId}/top")
    @Operation(summary = "cafeId에 해당하는 카페의 상단 정보 조회")
    public ResponseEntity<CafeTopResponse> findTop(@PathVariable Long cafeId,
                                                   HttpServletRequest servletRequest){

        Long memberId = getMemberId(servletRequest);
        CafeTopResponse response = cafeService.detailTop(cafeId, memberId);
        response.add(linkTo(methodOn(CafeController.class).findTop(cafeId, servletRequest)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).save(null, null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/findTop");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{cafeId}/basic")
    @Operation(summary = "cafeId에 해당하는 카페의 기본 정보 조회")
    public ResponseEntity<CafeBasicResponse> findBasic(@PathVariable Long cafeId) {

        CafeBasicResponse response = cafeService.findBasic(cafeId);
        response.add(linkTo(methodOn(CafeController.class).findBasic(cafeId)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).save(null, null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/findBasic");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "좌표 기반 카페 조회")
    public ResponseEntity<CafesResponse> findByCoordinates(@RequestParam BigDecimal latitude,
                                                           @RequestParam BigDecimal longitude,
                                                           HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafesResponse response = cafeService.findByCoordinates(latitude, longitude, memberId);
        response.add(linkTo(methodOn(CafeController.class).findByCoordinates(latitude, longitude, servletRequest)).withSelfRel())
                .add(linkTo(methodOn(CafeController.class).findTop(null, null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findBasic(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findMenus(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findImages(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findReviews(null)).withRel("detail").withType("POST"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinatesAndKeyword(null, null, null, null))
                        .withRel("search").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/findByCoordinates");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/keyword")
    @Operation(summary = "좌표/키워드 기반 카페 조회")
    public ResponseEntity<CafesResponse> findByCoordinatesAndKeyword(@RequestParam BigDecimal latitude,
                                                                     @RequestParam BigDecimal longitude,
                                                                     @RequestParam List<String> keywords,
                                                                     HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        CafesResponse response = cafeService.findByCoordinatesAndKeyword(latitude, longitude, keywords, memberId);
        response.add(linkTo(methodOn(CafeController.class).findByCoordinatesAndKeyword(latitude, longitude, keywords, servletRequest)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinates(null, null, null)).withRel("search").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findTop(null, null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findBasic(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findMenus(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findImages(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findReviews(null)).withRel("detail").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/findByCoordinatesAndKeyword");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "카페 저장")
    public ResponseEntity<CafeSaveResponse> save(@RequestBody AdminCafeSaveRequest request) {
        CafeSaveResponse response = cafeService.save(request);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/save");
        response.add(linkTo(methodOn(CafeController.class).save(request)).withSelfRel().withType("POST"));
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PutMapping("/{cafeId}")
    @Operation(summary = "cafeId에 해당하는 카페 수정")
    public ResponseEntity<CafeSaveResponse> update(@PathVariable Long cafeId,
                                                   @RequestBody AdminCafeUpdateRequest request) {

        CafeSaveResponse response = cafeService.update(cafeId, request);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/update");
        response.add(linkTo(methodOn(CafeController.class).update(cafeId, request)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(MenuController.class).delete(null)).withRel("delete").withType("DELETE"));
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{cafeId}/reviews")
    @Operation(summary = "cafeId에 해당하는 모든 리뷰 조회")
    public ResponseEntity<CafeReviewsResponse> findReviews(@PathVariable Long cafeId) {

        CafeReviewsResponse response = cafeService.findReviews(cafeId);
        response.add(linkTo(methodOn(CafeController.class).findReviews(cafeId)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).save(null, null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/findReviews");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{cafeId}/images")
    @Operation(summary = "cafeId에 해당하는 모든 이미지 조회")
    public ResponseEntity<ImagesResponse> findImages(@PathVariable Long cafeId) {

        ImagesResponse response = cafeService.findImages(cafeId);
        response.add(linkTo(methodOn(CafeController.class).findImages(cafeId)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).save(null, null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/findImages");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{cafeId}/menus")
    @Operation(summary = "cafeId에 해당하는 모든 메뉴 조회")
    public ResponseEntity<MenusResponse> findMenus(@PathVariable Long cafeId) {

        MenusResponse response = cafeService.findMenus(cafeId);
        response.add(linkTo(methodOn(CafeController.class).findMenus(cafeId)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(ReviewController.class).save(null, null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("cafes/findMenus");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/{kakaoId}")
    public ResponseEntity<Boolean> isExistByKakaoId(@PathVariable Long kakaoId) {
        boolean exists = cafeService.isExistByKakaoId(kakaoId);
        return ResponseEntity.ok(exists);
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
