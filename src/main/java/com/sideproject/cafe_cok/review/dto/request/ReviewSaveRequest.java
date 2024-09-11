package com.sideproject.cafe_cok.review.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "리뷰 저장 요청")
public class ReviewSaveRequest {

    @Schema(description = "리뷰를 작성한 카페 ID", example = "1")
    private Long cafeId;

    @Schema(description = "리뷰 내용", example = "리뷰 내용")
    private String content;

    @Schema(description = "리뷰 특이 사항", example = "리뷰 특이 사항")
    private String specialNote;

    @Schema(description = "리뷰 키워드 리스트", example = "데이트/모임")
    private List<String> keywords;

    @Schema(description = "리뷰 별점", example = "4")
    private Integer starRating;

    public ReviewSaveRequest(
            final Long cafeId, final String content, final String specialNote,
            final List<String> keywords, final Integer starRating) {
        this.cafeId = cafeId;
        this.content = content;
        this.specialNote = specialNote;
        this.keywords = keywords;
        this.starRating = starRating;
    }
}
