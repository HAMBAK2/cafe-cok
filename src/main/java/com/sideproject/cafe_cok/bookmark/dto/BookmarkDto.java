package com.sideproject.cafe_cok.bookmark.dto;

import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import lombok.Builder;
import lombok.Getter;
import org.springframework.aop.target.LazyInitTargetSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BookmarkDto {

    private Long bookmarkId;
    private Long cafeId;
    private String cafeName;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static BookmarkDto from(final Bookmark bookmark) {
        return BookmarkDto.builder()
                .bookmarkId(bookmark.getId())
                .cafeId(bookmark.getCafe().getId())
                .cafeName(bookmark.getCafe().getName())
                .roadAddress(bookmark.getCafe().getRoadAddress())
                .latitude(bookmark.getCafe().getLatitude())
                .longitude(bookmark.getCafe().getLongitude())
                .build();
    }

    public static List<BookmarkDto> fromList(final List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> BookmarkDto.from(bookmark))
                .collect(Collectors.toList());
    }
}
