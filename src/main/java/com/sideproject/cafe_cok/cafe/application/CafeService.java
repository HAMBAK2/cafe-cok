package com.sideproject.cafe_cok.cafe.application;

import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.enums.OpenStatus;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageUrlCursorDto;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.utils.Constants.*;
import static com.sideproject.cafe_cok.utils.FormatConverter.*;
import static com.sideproject.cafe_cok.utils.FormatConverter.getKoreanDayOfWeek;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;
    private final KeywordRepository keywordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final OperationHourRepository operationHourRepository;

    private static final Boolean HAS_NEXT_PAGE = true;
    private static final Boolean NO_NEXT_PAGE = false;

    public static final Integer CAFE_DETAIL_IMAGE_SIZE = 8;
    public static final Integer CAFE_DETAIL_CAFE_IMAGE_MAX_CNT = 3;
    public static final Integer CAFE_DETAIL_BASIC_INFO_IMAGE_MAX_CNT = 6;
    public static final Integer CAFE_DETAIL_BASIC_REVIEW_CNT = 2;
    public static final Integer CAFE_DETAIL_REVIEW_CNT = 5;
    public static final Integer ALL_LIST_CNT = Integer.MAX_VALUE;

    public CafeDetailImagePageResponse detailImages(final Long cafeId,
                                                    final Long cursor) {

        List<ImageUrlDto> images = new ArrayList<>();

        Pageable pageable;
        Integer cafeDetailImageSize = CAFE_DETAIL_IMAGE_SIZE;

        if(cursor == null) {
            List<ImageUrlDto> findCafeImageUrlDtoList = getCafeImageUrlDtoList(cafeId);
            cafeDetailImageSize -= findCafeImageUrlDtoList.size();
            images.addAll(findCafeImageUrlDtoList);
        }

        List<ImageUrlCursorDto> findReviewImageUrlCursorDtoList = imageRepository
                .findImageUrlCursorDtoListByCafeIdAndImageTypeOrderByIdDesc(
                        cafeId,
                        cursor,
                        ImageType.REVIEW,
                        PageRequest.of(0, cafeDetailImageSize));

        if(findReviewImageUrlCursorDtoList.size() < cafeDetailImageSize) {
            images.addAll(convertFromImageUrlCursorDtoToImageUrlDto(findReviewImageUrlCursorDtoList));
            return new CafeDetailImagePageResponse(images, NO_NEXT_PAGE);
        }

        Long newCursor = findReviewImageUrlCursorDtoList.get(findReviewImageUrlCursorDtoList.size() - 1).getCursor();
        images.addAll(convertFromImageUrlCursorDtoToImageUrlDto(findReviewImageUrlCursorDtoList));
        return new CafeDetailImagePageResponse(images, newCursor, HAS_NEXT_PAGE);
    }

    private List<ImageUrlDto> getCafeImageUrlDtoList(final Long cafeId) {
        return imageRepository
                .findCafeImageUrlDtoListByCafeId(
                        cafeId,
                        PageRequest.of(0, CAFE_DETAIL_CAFE_IMAGE_MAX_CNT));
    }

    public CafeDetailImageAllResponse detailImagesAll(final Long cafeId) {

        List<ImageUrlDto> images = new ArrayList<>();
        List<ImageUrlDto> findCafeImageUrlDtoList = getCafeImageUrlDtoList(cafeId);
        List<ImageUrlDto> findReviewImageUrlDtoList =
                imageRepository.findImageUrlDtoListByCafeIdImageType(cafeId, ImageType.REVIEW);

        images.addAll(findCafeImageUrlDtoList);
        images.addAll(findReviewImageUrlDtoList);
        return CafeDetailImageAllResponse.from(images);
    }

    public CafeDetailReviewPageResponse detailReviews(final Long cafeId,
                                                      final Long cursor) {

        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews;

        List<Review> findReviews = reviewRepository
                .findByCafeIdAndCursorOrderByIdDesc(
                        cafeId,
                        cursor,
                        PageRequest.of(0, CAFE_DETAIL_REVIEW_CNT));
        reviews = convertReviewsToCafeDetailReviewDtoList(cafeId, findReviews);

        if(reviews.size() == CAFE_DETAIL_REVIEW_CNT) {
            Long newCursor = reviews.get(reviews.size() - 1).getId();;
            return CafeDetailReviewPageResponse.of(userChoiceKeywords, reviews, newCursor, HAS_NEXT_PAGE);
        }

        return CafeDetailReviewPageResponse.of(userChoiceKeywords, reviews);
    }

    public CafeDetailReviewAllResponse detailReviewsAll(final Long cafeId) {
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtoList(cafeId, ALL_LIST_CNT);;
        return CafeDetailReviewAllResponse.of(userChoiceKeywords, reviews);
    }

    private List<CafeDetailReviewDto> getCafeDetailReviewDtoList(final Long cafeId,
                                                                 final Integer reviewCnt) {
        Pageable pageable = PageRequest.of(0, reviewCnt);
        List<Review> reviews = reviewRepository.findByCafeIdOrderByIdDesc(cafeId, pageable);
        return convertReviewsToCafeDetailReviewDtoList(cafeId, reviews);
    }

    private List<CafeDetailReviewDto> convertReviewsToCafeDetailReviewDtoList(final Long cafeId,
                                                                              final List<Review> reviews) {
        List<CafeDetailReviewDto> cafeDetailReviewDtoList = new ArrayList<>();
        for (Review review : reviews) {

            Pageable pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_KEYWORD_CNT);
            List<String> recommendMenus = keywordRepository
                    .findNamesByReviewIdAndCategory(review.getId(), Category.MENU, pageable);

            pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_IMG_CNT);
            List<ImageUrlDto> findImageUrlDtoList = imageRepository
                    .findImageUrlDtoListByCafeIdAndReviewIdOrderByIdDesc(cafeId, review.getId(), pageable);

            cafeDetailReviewDtoList.add(new CafeDetailReviewDto(review, findImageUrlDtoList, recommendMenus));
        }
        return cafeDetailReviewDtoList;
    }

    public CafeSearchResponse getCafeListByAgain(final BigDecimal latitude,
                                                 final BigDecimal longitude,
                                                 final Long memberId) {

        List<CafeDto> withinRadiusCafes = getWithinRadiusCafeDtoList(latitude, longitude, memberId);
        return new CafeSearchResponse(withinRadiusCafes);
    }

    public CafeSearchResponse findCafeByBar(final BigDecimal latitude,
                                            final BigDecimal longitude,
                                            final Long memberId) {

        List<CafeDto> withinRadiusCafes = getWithinRadiusCafeDtoList(latitude, longitude, memberId);
        CafeDto targetCafe = withinRadiusCafes.stream()
                .filter(cafe -> cafe.getLatitude().equals(latitude) && cafe.getLatitude().equals(longitude))
                .findFirst()
                .orElse(null);

        if(targetCafe != null) {
            withinRadiusCafes.remove(targetCafe);
            withinRadiusCafes.add(0, targetCafe);
        }
        return new CafeSearchResponse(withinRadiusCafes);
    }

    public CafeSearchResponse findCafeByKeyword(final CafeFindCategoryRequest request, final Long memberId) {

        List<CafeDto> withinRadiusCafes = getWithinRadiusCafeDtoList(request.getLatitude(), request.getLongitude(), memberId);
        List<String> targetKeywordNames = request.getKeywords();

        List<CafeDto> filteredWithinRadiusCafes = new ArrayList<>();
        for (CafeDto withinRadiusCafe : withinRadiusCafes) {
            List<String> findNames = keywordRepository.findNamesByCafeId(withinRadiusCafe.getId());
            boolean allMatch = targetKeywordNames.stream().allMatch(findNames::contains);
            if(allMatch) filteredWithinRadiusCafes.add(withinRadiusCafe);
        }

        return new CafeSearchResponse(filteredWithinRadiusCafes);
    }

    public CafeDetailMenuResponse detailMenus(final Long cafeId) {

        List<MenuImageUrlDto> findMenus = menuRepository.findMenuImageUrlDtoListByCafeId(cafeId);
        return new CafeDetailMenuResponse(findMenus);
    }

    public CafeDetailTopResponse detailTop(final Long cafeId,
                                           final Long memberId) {

        Cafe findCafe = cafeRepository.getById(cafeId);
        Image findImage = imageRepository.getImageByCafeAndImageType(findCafe, ImageType.CAFE_MAIN);
        List<KeywordDto> findKeywordDtoList =
                keywordRepository.findKeywordDtoListByCafeIdOrderByCountDesc(
                        cafeId, PageRequest.of(0, CAFE_DETAIL_TOP_KEYWORD_MAX_CNT));

        if(memberId != null) {
            List<BookmarkIdDto> findBookmarkIdDtoList
                    = bookmarkRepository.findBookmarkIdDtoListByCafeIdAndMemberId(cafeId, memberId);
            return new CafeDetailTopResponse(findCafe, findImage, findKeywordDtoList, findBookmarkIdDtoList);
        }
        return new CafeDetailTopResponse(findCafe, findImage, findKeywordDtoList);
    }

    public CafeDetailBasicInfoResponse detailBasicInfo(final Long cafeId) {

        Cafe findCafe = cafeRepository.getById(cafeId);
        List<OperationHour> findOperationHours = operationHourRepository.findByCafeId(cafeId);
        List<String> businessHours = new ArrayList<>();
        List<String> closedDay = new ArrayList<>();
        OpenStatus openStatus = OpenStatus.CLOSE;
        for (OperationHour findOperationHour : findOperationHours) {
            if(findOperationHour.isClosed()) {
                closedDay.add(getKoreanDayOfWeek(findOperationHour.getDate()));
                continue;
            }
            if(findOperationHour.getDate().equals(LocalDate.now().getDayOfWeek())) {
                openStatus = checkOpenStatus(findOperationHour);
                businessHours.add(0, convertOperationHourToString(findOperationHour));
                continue;
            }
            businessHours.add(convertOperationHourToString(findOperationHour));
        }

        List<MenuImageUrlDto> findMenuImageUrlDtoList = menuRepository.findMenuImageUrlDtoListByCafeId(cafeId);
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<ImageUrlDto> imageUrlDtoList = getImageUrlDtoListByCafeId(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtoList(cafeId, CAFE_DETAIL_BASIC_REVIEW_CNT);

        return new CafeDetailBasicInfoResponse(
                findCafe, openStatus, businessHours, closedDay,
                findMenuImageUrlDtoList, imageUrlDtoList, userChoiceKeywords, reviews);
    }

    private List<ImageUrlDto> convertFromImageUrlCursorDtoToImageUrlDto(final List<ImageUrlCursorDto> dtoList) {
        return dtoList.stream()
                .map(dto -> new ImageUrlDto(dto.getOriginUrl(), dto.getThumbnailUrl()))
                .collect(Collectors.toList());
    }

    private List<ImageUrlDto> getImageUrlDtoListByCafeId(final Long cafeId) {

        List<ImageUrlDto> imageUrlDtoList = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, CAFE_DETAIL_CAFE_IMAGE_MAX_CNT);
        List<ImageUrlDto> findCafeImageUrlDtoList = imageRepository.findCafeImageUrlDtoListByCafeId(cafeId, pageable);

        pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_IMAGE_MAX_CNT - findCafeImageUrlDtoList.size());
        List<ImageUrlDto> findReviewImageUrlDtoList =
                imageRepository.findImageUrlDtoListByCafeIdImageTypeAndPageable(cafeId, ImageType.REVIEW, pageable);

        imageUrlDtoList.addAll(findCafeImageUrlDtoList);
        imageUrlDtoList.addAll(findReviewImageUrlDtoList);
        return imageUrlDtoList;
    }

    private OpenStatus checkOpenStatus(final OperationHour operationHour) {
        if(operationHour.getOpeningTime().isAfter(LocalTime.now())
                && operationHour.getClosingTime().isBefore(LocalTime.now())) return OpenStatus.OPEN;
        return OpenStatus.CLOSE;
    }

    private List<KeywordCountDto> getUserChoiceKeywordCounts(final Long cafeId) {
        return keywordRepository.findKeywordCountDtoListByCafeIdOrderByCountDesc(
                cafeId, PageRequest.of(0, USER_CHOICE_KEYWORD_CNT));
    }

    private List<CafeDto> getWithinRadiusCafeDtoList(final BigDecimal latitude,
                                                     final BigDecimal longitude,
                                                     final Long memberId) {
        List<Cafe> findCafes = cafeRepository.findWithinRadiusCafeList(latitude, longitude);
        List<CafeDto> cafeDtoList = findCafes.stream()
                .map(cafe -> {
                    String findImageUrl =
                            imageRepository.getImageByCafeAndImageType(cafe, ImageType.CAFE_MAIN).getThumbnail();

                    List<BookmarkIdDto> findBookmarkIdDtoList = null;
                    if(memberId != null) findBookmarkIdDtoList =
                            bookmarkRepository.findBookmarkIdDtoListByCafeIdAndMemberId(cafe.getId(), memberId);
                    return new CafeDto(cafe, findImageUrl, findBookmarkIdDtoList);
                }).collect(Collectors.toList());
        return cafeDtoList;
    }
}
