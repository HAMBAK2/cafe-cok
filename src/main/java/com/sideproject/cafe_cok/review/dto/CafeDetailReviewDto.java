package com.sideproject.cafe_cok.review.dto;


import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 상세 리뷰 DTO")
public class CafeDetailReviewDto {

    @Schema(description = "리뷰 ID", example = "1")
    private Long id;

    @Schema(description = "리뷰 내용", example = "리뷰 내용")
    private String content;

    @Schema(description = "리뷰 별점", example = "4")
    private Integer starRating;

    @Schema(description = "리뷰 특이사항", example = "리뷰 특이사항")
    private String specialNote;

    @Schema(description = "리뷰 생성일자", example = "2024-07-22")
    private LocalDate createDate;

    @Schema(description = "리뷰 작성자 프로필 이미지 URL", example = "//리뷰_작성자_프로필_이미지_URL")
    private String picture;

    @Schema(description = "리뷰 작성자 닉네임", example = "닉네임")
    private String nickname;

    @Schema(description = "리뷰 이미지 리스트")
    private List<ImageUrlDto> imageUrls;

    @Schema(description = "리뷰에 선택된 추천메뉴 리스트", example = "아메리카노")
    private List<String> recommendMenus;

    public CafeDetailReviewDto(final Review review,
                               final List<ImageUrlDto> imageUrls,
                               final List<String> recommendMenus) {
        this.id = review.getId();
        this.content = review.getContent();
        this.starRating = review.getStarRating();
        this.specialNote = review.getSpecialNote();
        this.createDate = review.getCreatedDate().toLocalDate();
        this.picture = review.getMember().getPicture();
        this.nickname = review.getMember().getNickname();
        this.imageUrls = imageUrls;
        this.recommendMenus = recommendMenus;
    }
}
