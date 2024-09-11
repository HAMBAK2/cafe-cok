package com.sideproject.cafe_cok.cafe.dto.response;


import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 상단 정보 조회 응답")
public class CafeTopResponse extends RepresentationModel<CafeTopResponse> {

    @Schema(description = "카페 ID", example = "1")
    private Long cafeId;

    @Schema(description = "카페 이름", example = "카페 이름")
    private String cafeName;

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

    @Schema(description = "카페의 원본 이미지 URL", example = "//원본_이미지_URL")
    private String originUrl;

    @Schema(description = "카페의 썸네일 이미지 URL", example = "//썸네일_이미지_URL")
    private String thumbnailUrl;

    @Schema(description = "카페 키워드의 리스트")
    private List<KeywordDto> keywords;

    @Schema(description = "카페 북마크 관련 정보의 리스트")
    private List<BookmarkFolderIdsDto> bookmarks;

    @Builder
    public CafeTopResponse(final Long cafeId,
                           final String cafeName,
                           final String roadAddress,
                           final BigDecimal latitude,
                           final BigDecimal longitude,
                           final BigDecimal starRating,
                           final Long reviewCount,
                           final String originUrl,
                           final String thumbnailUrl,
                           final List<KeywordDto> keywords,
                           final List<BookmarkFolderIdsDto> bookmarks) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.starRating = starRating;
        this.reviewCount = reviewCount;
        this.originUrl = originUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.keywords = keywords;
        this.bookmarks = bookmarks;
    }
}
