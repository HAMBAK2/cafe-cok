package com.sideproject.cafe_cok.cafe.dto;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CafeDto {

    private Long id;
    private List<BookmarkIdDto> bookmarks;
    private String name;
    private String phoneNumber;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double starRating;
    private Long reviewCount;
    private String imageUrl;

    public static CafeDto from(final Cafe cafe) {
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(cafe.getReviewCount())
                .imageUrl(cafe.getImage(ImageType.CAFE_MAIN).getThumbnail())
                .build();
    }

    public static List<CafeDto> fromList(final List<Cafe> cafes) {
        return cafes.stream()
                .map(cafe -> CafeDto.from(cafe))
                .collect(Collectors.toList());
    }

    public static CafeDto of(final Cafe cafe,
                             final String imageUrl) {
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(cafe.getReviewCount())
                .imageUrl(imageUrl)
                .build();
    }

    public static CafeDto of(final Cafe cafe,
                             final String imageUrl,
                             final List<BookmarkIdDto> bookmarks) {
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(cafe.getReviewCount())
                .imageUrl(imageUrl)
                .bookmarks(bookmarks)
                .build();
    }

    public void setBookmarks(final List<BookmarkIdDto> bookmarks) {
        this.bookmarks = bookmarks;
    }

}
