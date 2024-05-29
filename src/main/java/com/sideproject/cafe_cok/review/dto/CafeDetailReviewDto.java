package com.sideproject.cafe_cok.review.dto;


import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CafeDetailReviewDto {

    private Long id;
    private String content;
    private Integer starRating;
    private String specialNote;
    private LocalDate createDate;
    private String picture;
    private String nickname;
    private List<ImageUrlDto> imageUrls;
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
