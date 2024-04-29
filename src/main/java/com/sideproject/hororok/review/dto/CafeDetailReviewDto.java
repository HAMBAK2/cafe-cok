package com.sideproject.hororok.review.dto;


import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CafeDetailReviewDto {

    private Long id;
    private String content;
    private Integer starRating;
    private String specialNote;
    private LocalDate createDate;
    private String picture;
    private String nickname;
    private List<String> imageUrls;
    private List<KeywordDto> recommendMenus;


    public static CafeDetailReviewDto of(
            final Review review, final List<String> imageUrls, final List<KeywordDto> keywords) {

        return CafeDetailReviewDto.builder()
                .id(review.getId())
                .starRating(review.getStarRating())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .createDate(review.getCreatedDate().toLocalDate())
                .picture(review.getMember().getPicture())
                .nickname(review.getMember().getNickname())
                .imageUrls(imageUrls)
                .recommendMenus(keywords)
                .build();
    }
}
