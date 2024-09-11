package com.sideproject.cafe_cok.cafe.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 상세 정보 DTO")
public class CafeDto {

    @Schema(description = "카페 ID", example = "1")
    private Long id;

    @Schema(description = "카페 이름", example = "카페 이름")
    private String name;

    @Schema(description = "카페 전화번호", example = "000-0000-0000")
    private String phoneNumber;

    @Schema(description = "카페 도로명 주소", example = "서울 종로구 종로5길 7")
    private String roadAddress;

    @Schema(description = "카페 위도", example = "37.57061772252790")
    private BigDecimal latitude;

    @Schema(description = "카페 경도", example = "126.98055287409800")
    private BigDecimal longitude;

    @Schema(description = "카페 별점", example = "4.5")
    private BigDecimal starRating;

    @Schema(description = "카페의 리뷰 개수", example = "12")
    private Long reviewCount;

    @Schema(description = "카페의 이미지 URL", example = "//카페_이미지_URL")
    private String imageUrl;

    @Schema(description = "카페 북마크 관련 정보의 리스트")
    private List<BookmarkFolderIdsDto> bookmarks;


    @Builder
    @QueryProjection
    public CafeDto(final Long id,
                   final String name,
                   final String phoneNumber,
                   final String roadAddress,
                   final BigDecimal latitude,
                   final BigDecimal longitude,
                   final BigDecimal starRating,
                   final Long reviewCount,
                   final String imageUrl,
                   final List<BookmarkFolderIdsDto> bookmarks) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.starRating = starRating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.bookmarks = bookmarks;
    }

    public void setBookmarks(final List<BookmarkFolderIdsDto> bookmarks) {
        this.bookmarks = bookmarks;
    }

}
