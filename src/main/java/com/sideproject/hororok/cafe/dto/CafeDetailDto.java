package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.Menu.dto.MenuDto;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.image.dto.ImageDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.utils.calculator.BusinessHoursUtils;
import lombok.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CafeDetailDto {

    private final Long id;
    private final String cafeName;
    private final String roadAddress;
    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final List<String> BusinessHours;
    private final List<String> closedDay;
    private final List<String> cafeImageUrls;
    private final String openStatus;
    private final String phoneNumber;

    private final List<MenuDto> menus;
    private final List<ReviewDto> reviews;
    private final List<ImageDto> images;
    private final List<KeywordDto> cafeKeywords;

    public static CafeDetailDto of(Cafe cafe, List<MenuDto> menus,
                                   List<ImageDto> images, List<ReviewDto> reviews,
                                   List<KeywordDto> cafeKeywords, List<String> cafeImageUrls) {


        return CafeDetailDto.builder()
                .id(cafe.getId())
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .BusinessHours(Arrays.asList(cafe.getBusinessHours().split(", ")))
                .closedDay(BusinessHoursUtils.closedDaysConvert(cafe.getClosedDay()))
                .cafeImageUrls(cafeImageUrls)
                .openStatus(BusinessHoursUtils.getBusinessStatus(cafe.getBusinessHours()))
                .phoneNumber(cafe.getPhoneNumber())
                .menus(menus)
                .reviews(reviews)
                .images(images)
                .cafeKeywords(cafeKeywords)
                .build();
    }

}
