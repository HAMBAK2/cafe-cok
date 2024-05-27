package com.sideproject.cafe_cok.cafe.application;

import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.enums.OpenStatus;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.cafe.exception.NoSuchCafeException;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.keword.domain.CafeReviewKeyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.FormatConverter;
import com.sideproject.cafe_cok.utils.GeometricUtils;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;


    private static final Boolean HAS_NEXT_PAGE = true;
    private static final Boolean NO_NEXT_PAGE = false;
    private static final Long NO_CURSOR = null;
    public static final Integer CAFE_DETAIL_IMAGE_MAX_CNT = 2;
    public static final Integer CAFE_DETAIL_BASIC_INFO_IMAGE_MAX_CNT = 6;
    public static final Integer CAFE_DETAIL_BASIC_REVIEW_CNT = 2;
    public static final Integer CAFE_DETAIL_BASIC_MENU_CNT = 2;
    public static final Integer CAFE_DETAIL_REVIEW_CNT = 5;
    public static final Integer ALL_LIST_CNT = Integer.MAX_VALUE;

    public CafeFindAgainResponse findCafeByAgain(
            final BigDecimal latitude, final BigDecimal longitude, final Long memberId) {

        List<CafeDto> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
        if(memberId != null) setCafeDtosBookmarkId(withinRadiusCafes, memberId);
        return CafeFindAgainResponse.from(withinRadiusCafes);
    }

    public CafeFindBarResponse findCafeByBar(final BigDecimal latitude, final BigDecimal longitude, final Long memberId) {

        List<CafeDto> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
        CafeDto targetCafe = null;
        for (CafeDto withinRadiusCafe : withinRadiusCafes) {
            if(withinRadiusCafe.getLatitude().equals(latitude) && withinRadiusCafe.getLongitude().equals(longitude)) {
                targetCafe = withinRadiusCafe;
                break;
            }
        }

        if(targetCafe != null) {
            withinRadiusCafes.remove(targetCafe);
            withinRadiusCafes.add(0, targetCafe);
        }
        if(memberId != null) setCafeDtosBookmarkId(withinRadiusCafes, memberId);

        return CafeFindBarResponse.from(withinRadiusCafes);
    }

    public CafeFindCategoryResponse findCafeByKeyword(final CafeFindCategoryRequest request, final Long memberId) {

        List<CafeDto> withinRadiusCafes = findWithinRadiusCafes(request.getLatitude(), request.getLongitude());
        List<String> targetKeywordNames = request.getKeywords();
        List<CafeDto> filteredWithinRadiusCafes = new ArrayList<>();

        for (CafeDto withinRadiusCafe : withinRadiusCafes) {
            Cafe cafe = cafeRepository.findById(withinRadiusCafe.getId()).get();
            List<Review> reviews = reviewRepository.findByCafeId(cafe.getId());
            List<CafeReviewKeyword> findCafeReviewKeyword = cafeReviewKeywordRepository.findByReviewIn(reviews);
            List<String> keywordNames = findCafeReviewKeyword.stream()
                    .map(cafeReviewKeyword -> cafeReviewKeyword.getKeyword().getName())
                    .distinct()
                    .collect(Collectors.toList());

            boolean allMatch = targetKeywordNames.stream().allMatch(keywordNames::contains);
            if(allMatch) filteredWithinRadiusCafes.add(withinRadiusCafe);
        }
        if(memberId != null) setCafeDtosBookmarkId(withinRadiusCafes, memberId);

        return CafeFindCategoryResponse.from(filteredWithinRadiusCafes);
    }

    public CafeDetailTopResponse detailTop(final Long cafeId, final Long memberId) {

        Cafe findCafe = cafeRepository.getById(cafeId);
        Image findImage = imageRepository.getImageByCafeAndImageType(findCafe, ImageType.CAFE_MAIN);
        Long reviewCount = reviewRepository.countReviewByCafeId(cafeId);
        List<KeywordDto> findKeywordDtos = KeywordDto
                .fromList(keywordRepository.findKeywordsByCafeIdOrderByCountDesc(
                        cafeId, PageRequest.of(0, Constants.CAFE_DETAIL_TOP_KEYWORD_MAX_CNT)));

        if(memberId != null) {
            List<Bookmark> findBookmarks = bookmarkRepository.findByCafeIdAndMemberId(cafeId, memberId);
            return CafeDetailTopResponse
                    .of(findCafe, BookmarkIdDto.fromList(findBookmarks), findImage, reviewCount, findKeywordDtos);
        }
        return CafeDetailTopResponse.of(findCafe, findImage, reviewCount, findKeywordDtos);
    }

    public CafeDetailBasicInfoResponse detailBasicInfo(final Long cafeId) {

        Cafe findCafe = cafeRepository.getById(cafeId);
        OpenStatus openStatus = getOpenStatusByCafeId(cafeId);
        List<String> businessHours = getBusinessHoursByCafeId(cafeId);
        List<String> closedDay = getCloseDayByCafeId(cafeId);
        List<Menu> findMenus = menuRepository.findByCafeId(cafeId);
        List<MenuImageUrlDto> menus = new ArrayList<>();
        for (Menu findMenu : findMenus) {
            Image findImage = imageRepository.getImageByMenuAndImageType(findMenu, ImageType.MENU);
            menus.add(new MenuImageUrlDto(findMenu, findImage));
        }
        List<ImageUrlDto> imageUrls = getImageUrlsByCafeIdAndReviewImageCnt(cafeId, CAFE_DETAIL_BASIC_INFO_IMAGE_MAX_CNT);
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtos(cafeId, CAFE_DETAIL_BASIC_REVIEW_CNT);

        return CafeDetailBasicInfoResponse
                .of(findCafe, openStatus, businessHours, closedDay, menus, imageUrls, userChoiceKeywords, reviews);
    }

    public CafeDetailMenuResponse detailMenus(final Long cafeId) {

        List<Menu> findMenus = menuRepository.findByCafeId(cafeId);
        List<MenuImageUrlDto> menuDtos = new ArrayList<>();

        for (Menu findMenu : findMenus) {
            Image findImage = imageRepository.getImageByMenuAndImageType(findMenu, ImageType.MENU);
            menuDtos.add(new MenuImageUrlDto(findMenu, findImage));
        }

        return CafeDetailMenuResponse.from(menuDtos);
    }

    public CafeDetailImagePageResponse detailImages(final Long cafeId, final Long cursor) {

        if(cursor == null) return getDetailImagesWhenFirstPage(cafeId);
        return getDetailImagesWhenNotFirstPage(cafeId, cursor);
    }

    private CafeDetailImagePageResponse getDetailImagesWhenFirstPage(final Long cafeId) {

        List<ImageUrlDto> images = new ArrayList<>();
        Image findCafeMainImage = imageRepository.getImageByCafeIdAndImageType(cafeId, ImageType.CAFE_MAIN);
        images.add(ImageUrlDto.from(findCafeMainImage));

        Pageable pageable = PageRequest.of(0, CAFE_DETAIL_IMAGE_MAX_CNT);
        List<Image> findCafeImages = imageRepository.findImagesByCafeIdAndImageType(cafeId, ImageType.CAFE, pageable);
        for (Image findCafeImage : findCafeImages) images.add(ImageUrlDto.from(findCafeImage));

        pageable = PageRequest.of(0, Constants.CAFE_DETAIL_IMAGE_SIZE - images.size());
        Page<Image> findReviewImagePage = imageRepository
                .findPageByCafeIdAndImageTypeOrderByIdDesc(cafeId, ImageType.REVIEW, pageable);

        if(!findReviewImagePage.hasContent()) return CafeDetailImagePageResponse.of(images, NO_NEXT_PAGE);

        List<Image> findReviewImages = findReviewImagePage.getContent();
        for (Image reviewImage : findReviewImages) images.add(ImageUrlDto.from(reviewImage));

        if(findReviewImages.size() < pageable.getPageSize()) return CafeDetailImagePageResponse.of(images, NO_NEXT_PAGE);

        Long newCursor = findReviewImages.get(findReviewImages.size() - 1).getId();
        return CafeDetailImagePageResponse.of(images, newCursor, HAS_NEXT_PAGE);
    }

    private CafeDetailImagePageResponse getDetailImagesWhenNotFirstPage(final Long cafeId, final Long cursor) {

        List<ImageUrlDto> imageUrls = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, Constants.CAFE_DETAIL_IMAGE_SIZE);
        Page<Image> findReviewImagePage = imageRepository.findPageByCafeIdOrderByIdDesc(cafeId, pageable, cursor);

        if(!findReviewImagePage.hasContent()) return CafeDetailImagePageResponse.of(imageUrls, NO_NEXT_PAGE);

        List<Image> findReviewImages = findReviewImagePage.getContent();
        for (Image reviewImage : findReviewImages) imageUrls.add(ImageUrlDto.from(reviewImage));
        if(findReviewImages.size() < pageable.getPageSize()) return CafeDetailImagePageResponse.of(imageUrls, NO_NEXT_PAGE);

        Long newCursor = findReviewImages.get(findReviewImages.size() - 1).getId();
        return CafeDetailImagePageResponse.of(imageUrls, newCursor, HAS_NEXT_PAGE);
    }

    public CafeDetailImageAllResponse detailImagesAll(final Long cafeId) {

        List<ImageUrlDto> imageUrls = new ArrayList<>();

        Image findCafeMainImage = imageRepository.getImageByCafeIdAndImageType(cafeId, ImageType.CAFE_MAIN);
        imageUrls.add(ImageUrlDto.from(findCafeMainImage));

        Pageable pageable = PageRequest.of(0, CAFE_DETAIL_IMAGE_MAX_CNT);
        List<Image> findCafeImages = imageRepository.findImagesByCafeIdAndImageType(cafeId, ImageType.CAFE, pageable);
        for (Image cafeImage : findCafeImages) imageUrls.add(ImageUrlDto.from(cafeImage));

        List<Image> findCafeReviewImages = imageRepository.findImagesByCafeIdAndImageType(cafeId, ImageType.REVIEW);
        for (Image cafeReviewImage : findCafeReviewImages) imageUrls.add(ImageUrlDto.from(cafeReviewImage));

        return CafeDetailImageAllResponse.from(imageUrls);
    }

    public CafeDetailReviewPageResponse detailReviews(final Long cafeId, final Long cursor) {

        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews;
        if(cursor == NO_CURSOR) reviews = getCafeDetailReviewDtos(cafeId, CAFE_DETAIL_REVIEW_CNT);
        else reviews = getCafeDetailReviewDtos(cafeId, CAFE_DETAIL_REVIEW_CNT, cursor);

        if(reviews.size() == CAFE_DETAIL_REVIEW_CNT) {
            Long newCursor = reviews.get(reviews.size() - 1).getId();;
            return CafeDetailReviewPageResponse.of(userChoiceKeywords, reviews, newCursor, HAS_NEXT_PAGE);
        }

        return CafeDetailReviewPageResponse.of(userChoiceKeywords, reviews);
    }

    public CafeDetailReviewAllResponse detailReviewsAll(final Long cafeId) {
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtos(cafeId, ALL_LIST_CNT);;
        return CafeDetailReviewAllResponse.of(userChoiceKeywords, reviews);
    }

    private List<CafeDetailReviewDto> getCafeDetailReviewDtos(final Long cafeId, final Integer reviewCnt) {
        Pageable pageable = PageRequest.of(0, reviewCnt);
        List<Review> reviews = reviewRepository.findByCafeIdOrderByIdDesc(cafeId, pageable);
        return convertReviewsToCafeDetailReviewDtos(cafeId, reviews);
    }

    private List<CafeDetailReviewDto> getCafeDetailReviewDtos(final Long cafeId, final Integer reviewCnt, Long cursor) {

        Pageable pageable = PageRequest.of(0, reviewCnt);
        List<Review> reviews;
        if(cursor == NO_CURSOR) reviews = reviewRepository.findByCafeIdOrderByIdDesc(cafeId, pageable);
        else reviews = reviewRepository.findByCafeIdOrderByIdDesc(cafeId, pageable, cursor).getContent();

        if(reviews.size() < pageable.getPageSize()) return convertReviewsToCafeDetailReviewDtos(cafeId, reviews);
        return convertReviewsToCafeDetailReviewDtos(cafeId, reviews);
    }

    private List<CafeDetailReviewDto> convertReviewsToCafeDetailReviewDtos(final Long cafeId, final List<Review> reviews) {
        List<CafeDetailReviewDto> cafeDetailReviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            List<String> recommendMenus = keywordRepository
                    .findNameByReviewIdAndCategory(review.getId(), Category.MENU,
                            PageRequest.of(0, Constants.CAFE_DETAIL_BASIC_INFO_REVIEW_KEYWORD_CNT));

            List<Image> findImages = imageRepository.findImagesByCafeIdAndImageTypeOrderByIdDescOrderByIdDesc(
                    cafeId, ImageType.REVIEW,
                    PageRequest.of(0, Constants.CAFE_DETAIL_BASIC_INFO_REVIEW_IMG_CNT));

            List<ImageUrlDto> imageUrls = findImages.stream()
                    .map(image -> ImageUrlDto.from(image))
                    .collect(Collectors.toList());

            cafeDetailReviewDtos.add(CafeDetailReviewDto.of(review, imageUrls, recommendMenus));
        }
        return cafeDetailReviewDtos;
    }


    private List<KeywordCountDto> getUserChoiceKeywordCounts(Long cafeId) {
        List<KeywordCountDto> allCafeKeywordCountDtos
                = keywordRepository.findKeywordCountsByCafeId(cafeId);

        if (allCafeKeywordCountDtos != null && allCafeKeywordCountDtos.size() > Constants.USER_CHOICE_KEYWORD_CNT) {
            allCafeKeywordCountDtos = allCafeKeywordCountDtos.subList(0, Constants.USER_CHOICE_KEYWORD_CNT);
        }

        return allCafeKeywordCountDtos;
    }

    private List<String> getBusinessHoursByCafeId(final Long cafeId) {
        List<OperationHour> businessHours = operationHourRepository.findBusinessHoursByCafeId(cafeId);
        List<String> convertedBusinessHours = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (OperationHour businessHour : businessHours) {
            String input;
            String date = FormatConverter.getKoreanDayOfWeek(businessHour.getDate());
            String openingTime = businessHour.getOpeningTime().format(formatter);
            String closingTime = businessHour.getClosingTime().format(formatter);

            input = date + " " + openingTime + "~" + closingTime;
            convertedBusinessHours.add(input);
        }
        return convertedBusinessHours;
    }

    private OpenStatus getOpenStatusByCafeId(final Long cafeId) {
        Optional<OperationHour> findOperationHour =
                operationHourRepository
                        .findByCafeIdAndDateAndTime(cafeId, LocalDate.now().getDayOfWeek(), LocalTime.now());

        if(findOperationHour.isEmpty()) return OpenStatus.CLOSE;
        return OpenStatus.OPEN;
    }

    public List<String> getCloseDayByCafeId(Long cafeId) {

        List<OperationHour> findDays = operationHourRepository.findClosedDayByCafeId(cafeId);
        List<String> closeDays = new ArrayList<>();

        if(findDays.isEmpty()) return closeDays;

        for (OperationHour findDay : findDays) {
            DayOfWeek date = findDay.getDate();
            closeDays.add(FormatConverter.getKoreanDayOfWeek(date));
        }

        return closeDays;
    }

    private List<ImageUrlDto> getImageUrlsByCafeIdAndReviewImageCnt(final Long cafeId, final Integer reviewImageCnt) {

        List<ImageUrlDto> combinedImageDtos = new ArrayList<>();

        Image findCafeMainImage = imageRepository.getImageByCafeIdAndImageType(cafeId, ImageType.CAFE_MAIN);
        combinedImageDtos.add(ImageUrlDto.from(findCafeMainImage));

        Pageable pageable = PageRequest.of(0, CAFE_DETAIL_IMAGE_MAX_CNT);
        List<Image> findCafeImages = imageRepository.findImagesByCafeIdAndImageType(cafeId, ImageType.CAFE, pageable);
        findCafeImages.stream().forEach(image -> combinedImageDtos.add(ImageUrlDto.from(image)));

        pageable = PageRequest.of(0, reviewImageCnt - combinedImageDtos.size());
        Page<Image> findReviewImagePage = imageRepository
                .findPageUrlsByCafeIdAndImageTypeOrderByIdDescOrderByIdDesc(cafeId, ImageType.REVIEW, pageable);
        if(findReviewImagePage.hasContent()) {
            List<Image> findReviewImages = findReviewImagePage.getContent();
            findReviewImages.stream().forEach(image -> combinedImageDtos.add(ImageUrlDto.from(image)));
        }

        return combinedImageDtos;
    }

    private List<CafeDto> findWithinRadiusCafes(final BigDecimal latitude, final BigDecimal longitude) {
        List<Cafe> cafes = cafeRepository.findAll();
        List<CafeDto> withinRadiusCafes = new ArrayList<>();

        for (Cafe cafe : cafes) {
            boolean isWithinRadius = GeometricUtils.isWithinRadius(
                            latitude, longitude,
                            cafe.getLatitude(), cafe.getLongitude(), Constants.MAX_RADIUS);
            Image findImage = imageRepository.getImageByCafeAndImageType(cafe, ImageType.CAFE_MAIN);
            if(isWithinRadius) withinRadiusCafes.add(CafeDto.of(cafe, findImage.getThumbnail()));
        }
        return withinRadiusCafes;
    }

    private List<CafeDto> setCafeDtosBookmarkId(final List<CafeDto> cafes, final Long memberId) {

        for (CafeDto cafe : cafes) {
            List<Bookmark> bookmarks = bookmarkRepository.findByCafeIdAndMemberId(cafe.getId(), memberId);
            cafe.setBookmarks(BookmarkIdDto.fromList(bookmarks));
        }
        return cafes;
    }

    public Cafe findById(final Long id) {

        return cafeRepository.findById(id)
                .orElseThrow(NoSuchCafeException::new);
    }
}
