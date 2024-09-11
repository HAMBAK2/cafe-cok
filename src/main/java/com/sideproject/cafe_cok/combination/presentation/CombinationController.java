package com.sideproject.cafe_cok.combination.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.cafe.presentation.CafeController;
import com.sideproject.cafe_cok.combination.application.CombinationService;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.combination.dto.response.CombinationResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationIdResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationListResponse;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/combinations")
@Tag(name = "combinations", description = "조합 API")
@ApiResponse(responseCode = "200", description = "성공")
public class CombinationController {

    private final CombinationService combinationService;
    private final HttpHeadersUtil httpHeadersUtil;

    @GetMapping
    @Operation(summary = "조합 목록 조회")
    public ResponseEntity<CombinationListResponse> findList(@AuthenticationPrincipal LoginMember loginMember) {

        CombinationListResponse response = combinationService.combination(loginMember);
        response.add(linkTo(methodOn(CombinationController.class).findList(loginMember)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(CombinationController.class).detail(null, null)).withRel("detail").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("combinations/findList");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "조합 저장")
    public ResponseEntity<CombinationIdResponse> save(@AuthenticationPrincipal LoginMember loginMember,
                                                      @RequestBody CombinationRequest request) {

        CombinationIdResponse response = combinationService.save(request, loginMember);
        response.add(linkTo(methodOn(CombinationController.class).save(loginMember, request)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(CombinationController.class).findList(loginMember)).withRel("list").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("combinations/save");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{combinationId}")
    @Operation(summary = "combinationId에 해당하는 조합 조회")
    @Parameter(name = "combinationId", description = "조회하려는 조합의 ID", example = "1")
    public ResponseEntity<CombinationResponse> detail(@AuthenticationPrincipal LoginMember loginMember,
                                                      @PathVariable Long combinationId) {

        CombinationResponse response = combinationService.find(combinationId);
        response.add(linkTo(methodOn(CombinationController.class).detail(loginMember, combinationId)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(CombinationController.class).update(null, null, null)).withRel("update").withType("PATCH"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("combinations/detail");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PatchMapping("/{combinationId}")
    @Operation(summary = "combinationId에 해당하는 조합 수정")
    @Parameter(name = "combinationId", description = "조회하려는 조합의 ID", example = "1")
    public ResponseEntity<CombinationIdResponse> update(@AuthenticationPrincipal LoginMember loginMember,
                                                        @PathVariable Long combinationId,
                                                        @RequestBody CombinationRequest request) {

        CombinationIdResponse response = combinationService.update(request, combinationId);
        response.add(linkTo(methodOn(CombinationController.class).update(loginMember, combinationId, request)).withSelfRel().withType("PATCH"))
                .add(linkTo(methodOn(CombinationController.class).detail(null, null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(CombinationController.class).findList(null)).withRel("list").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("combinations/update");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

}
