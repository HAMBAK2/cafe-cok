package com.sideproject.cafe_cok.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Schema(description = "북마크로 지정된 카페 정보")
public class BookmarkCafeDto {

    @Schema(description = "북마크 ID", example = "1")
    private Long bookmarkId;

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

    public BookmarkCafeDto(final Long bookmarkId,
                           final Cafe cafe) {
        this.bookmarkId = bookmarkId;
        this.cafeId = cafe.getId();
        this.cafeName = cafe.getName();
        this.roadAddress = cafe.getRoadAddress();
        this.latitude = cafe.getLatitude();
        this.longitude = cafe.getLongitude();
    }
}
