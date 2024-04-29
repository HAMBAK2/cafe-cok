package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.cafe.domain.OperationHour;
import com.sideproject.hororok.cafe.domain.repository.CafeImageRepository;
import com.sideproject.hororok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.hororok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.hororok.cafe.dto.response.*;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.keword.dto.KeywordCountDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.menu.domain.repository.MenuRepository;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.menu.application.MenuService;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.review.application.ReviewImageService;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.application.ReviewService;
import com.sideproject.hororok.cafe.domain.enums.OpenStatus;
import com.sideproject.hororok.review.domain.repository.ReviewImageRepository;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import com.sideproject.hororok.review.dto.CafeDetailReviewDto;
import com.sideproject.hororok.review.dto.ReviewImageDto;
import com.sideproject.hororok.utils.FormatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.sideproject.hororok.utils.Constants.*;
import static com.sideproject.hororok.utils.GeometricUtils.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final MenuRepository menuRepository;
    private final KeywordRepository keywordRepository;
    private final CafeImageRepository cafeImageRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final OperationHourRepository operationHourRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    private final MenuService menuService;
    private final ReviewService reviewService;
    private final KeywordService keywordService;
    private final CafeRepository cafeRepository;
    private final CafeImageService cafeImageService;
    private final ReviewImageService reviewImageService;
    private final OperationHourService operationHourService;
    
    private void addReviewImageUrlsToCafeImageUrls(List<String> cafeImageUrls, List<String> reviewImageUrls){

        int idx = 0;
        while(idx < reviewImageUrls.size() && idx < 2) {
            cafeImageUrls.add(reviewImageUrls.get(idx));
            idx++;
        }
    }

    public CafeHomeResponse home() {
        CategoryKeywordsDto allCategoryKeywords = keywordService.getAllCategoryKeywords();
        return new CafeHomeResponse(allCategoryKeywords);
    }

    
    public CafeFindAgainResponse findCafeByAgain(BigDecimal latitude, BigDecimal longitude) {

        List<WithinRadiusCafeDto> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
        CategoryKeywordsDto categoryKeywordsDto = keywordService.getAllCategoryKeywords();

        return CafeFindAgainResponse.of(withinRadiusCafes, categoryKeywordsDto);
    }

    
    public CafeFindBarResponse findCafeByBar(BigDecimal latitude, BigDecimal longitude) {

        Optional<Cafe> findCafe = cafeRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(findCafe.isEmpty()) {
            List<WithinRadiusCafeDto> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
            CategoryKeywordsDto categoryKeywordsDto = keywordService.getAllCategoryKeywords();
            return CafeFindBarResponse.notExistOf(withinRadiusCafes, categoryKeywordsDto);
        }

        return CafeFindBarResponse
                .existFrom(findCafeDetailByCafeId(findCafe.get().getId()));
    }

    
    public CafeFindCategoryResponse findCafeByKeyword(CafeFindCategoryRequest request) {

        List<WithinRadiusCafeDto> withinRadiusCafes = findWithinRadiusCafes(request.getLatitude(), request.getLongitude());
        CategoryKeywordsDto categoryKeywordsDto = keywordService.getAllCategoryKeywords();

        List<String> targetKeywordNames = request.getKeywords();
        List<WithinRadiusCafeDto> filteredWithinRadiusCafes = new ArrayList<>();
        for (WithinRadiusCafeDto withinRadiusCafe : withinRadiusCafes) {
            Cafe cafe = cafeRepository.findById(withinRadiusCafe.getId()).get();
            List<Review> reviews = cafe.getReviews();
            List<String> keywordNames = new ArrayList<>();
            for (Review review : reviews) {

                List<CafeReviewKeyword> cafeReviewKeywords = cafeReviewKeywordRepository.findByReviewId(review.getId());
                keywordNames = cafeReviewKeywords.stream()
                        .map(cafeReviewKeyword -> cafeReviewKeyword.getKeyword().getName())
                        .distinct()
                        .collect(Collectors.toList());

            }

            boolean allMatch = targetKeywordNames.stream()
                    .allMatch(keywordNames::contains);

            if(allMatch) filteredWithinRadiusCafes.add(withinRadiusCafe);
        }

        return CafeFindCategoryResponse.builder()
                .categoryKeywords(categoryKeywordsDto)
                .cafes(filteredWithinRadiusCafes)
                .build();
    }

    public CafeDetailTopResponse detailTop(final Long cafeId) {
        Cafe findCafe = cafeRepository.getById(cafeId);
        Long reviewCount = reviewRepository.countReviewByCafeId(cafeId);
        List<KeywordDto> findKeywordDtos = KeywordDto.fromList(keywordRepository
                .findKeywordsByCafeIdOrderByCountDesc(cafeId,
                        PageRequest.of(0, CAFE_DETAIL_TOP_KEYWORD_MAX_CNT)));

        return CafeDetailTopResponse.of(findCafe, reviewCount, findKeywordDtos);
    }

    public CafeDetailBasicInfoResponse detailBasicInfo(final Long cafeId) {

        Cafe findCafe = cafeRepository.getById(cafeId);
        OpenStatus openStatus = getOpenStatusByCafeId(cafeId);
        List<String> businessHours = getBusinessHoursByCafeId(cafeId);
        List<String> closedDay = getCloseDayByCafeId(cafeId);
        List<MenuDto> menus = MenuDto.fromList(menuRepository
                .findByCafeId(cafeId, PageRequest.of(0, CAFE_DETAIL_BASIC_MENU_CNT)));
        List<String> imageUrls = getDetailBasicInfoImageUrls(cafeId);
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtosByCafeId(cafeId);

        return CafeDetailBasicInfoResponse
                .of(findCafe, openStatus, businessHours, closedDay, menus,
                        imageUrls, userChoiceKeywords, reviews);
    }

    public List<CafeDetailReviewDto> getCafeDetailReviewDtosByCafeId(final Long cafeId) {
        List<Review> reviews = reviewRepository
                .findByCafeIdOrderByCreatedDateDesc(cafeId, PageRequest.of(0, CAFE_DETAIL_BASIC_REVIEW_CNT));
        List<CafeDetailReviewDto> cafeDetailReviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            List<KeywordDto> keywordDtos =
                    KeywordDto.fromList(keywordRepository
                            .findByReviewIdAndCategory(review.getId(), Category.MENU,
                                    PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_KEYWORD_CNT)));
            List<ReviewImageDto> images = ReviewImageDto.fromList(reviewImageRepository
                    .findByCafeIdOrderByCreatedDateDesc(cafeId,
                            PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_IMG_CNT)));
            cafeDetailReviewDtos.add(CafeDetailReviewDto.of(review, images, keywordDtos));
        }
        return cafeDetailReviewDtos;
    }

    public List<KeywordCountDto> getUserChoiceKeywordCounts(Long cafeId) {
        List<KeywordCountDto> allCafeKeywordCountDtos
                = keywordRepository.findKeywordCountsByCafeId(cafeId);

        if (allCafeKeywordCountDtos != null && allCafeKeywordCountDtos.size() > USER_CHOICE_KEYWORD_CNT) {
            allCafeKeywordCountDtos = allCafeKeywordCountDtos.subList(0, USER_CHOICE_KEYWORD_CNT);
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

    private List<String> getDetailBasicInfoImageUrls(final Long cafeId) {

        List<String> combinedImageUrls = new ArrayList<>();

        List<String> cafeImageUrls = cafeImageRepository
                        .findImageUrlsByCafeId(cafeId, PageRequest.of(0, CAFE_DETAIL_IMAGE_MAX_CNT));

        List<String> reviewImageUrls = reviewImageRepository
                .findImageUrlsByCafeIdOrderByCreatedDateDesc(cafeId,
                        PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_IMAGE_MAX_CNT - cafeImageUrls.size()));

        combinedImageUrls.addAll(cafeImageUrls);
        combinedImageUrls.addAll(reviewImageUrls);

        return combinedImageUrls;

    }
    
    public CafeDetail findCafeDetailByCafeId(Long cafeId){

        Cafe cafe =  cafeRepository.getById(cafeId);
        List<MenuDto> menus = menuService.findByCafeId(cafeId);
        List<String> cafeImageUrls = cafeImageService.findCafeImageUrlsByCafeId(cafeId);
        List<CafeDetailReviewDto> reviews = reviewService.getCafeDetailReviewDtosByCafeId(cafeId);
        List<String> reviewImageUrls = reviewImageService.getReviewImageUrlsByCafeId(cafeId);

        //총 6개의 키워드를 뽑아내고 그 중 3개는 카페 키워드로 사용해야 함
        List<KeywordCountDto> keywordCountDtos = keywordService.getUserChoiceKeywordCounts(cafeId);

        addReviewImageUrlsToCafeImageUrls(cafeImageUrls, reviewImageUrls);

        OpenStatus openStatus = operationHourService.getOpenStatus(cafeId);
        List<String> closedDay = operationHourService.getClosedDay(cafeId);
        List<String> businessHours = operationHourService.getBusinessHours(cafeId);

        return new CafeDetail(
                cafe, menus, openStatus, businessHours, closedDay,
                reviewImageUrls, reviews, keywordCountDtos, cafeImageUrls);
    }

    
    public List<WithinRadiusCafeDto> findWithinRadiusCafes(BigDecimal latitude, BigDecimal longitude) {
        List<Cafe> cafes = cafeRepository.findAll();
        List<WithinRadiusCafeDto> withinRadiusCafes = new ArrayList<>();

        for (Cafe cafe : cafes) {
            boolean isWithinRadius = isWithinRadius(
                            latitude, longitude,
                            cafe.getLatitude(), cafe.getLongitude());

            if(isWithinRadius) {
                String cafeImageUrl =
                        cafeImageService.findOneImageUrlByCafeId(cafe.getId())
                                .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));
                withinRadiusCafes.add(WithinRadiusCafeDto.of(cafe, cafeImageUrl));
            }
        }

        return withinRadiusCafes;
    }

    public List<Cafe> getFilteredCafesByWithinRadius(
            final BigDecimal latitude, final BigDecimal longitude, final List<Cafe> cafes) {

        return cafes.stream()
                .filter(cafe -> isWithinRadius(latitude, longitude, cafe.getLatitude(), cafe.getLongitude()))
                .collect(Collectors.toList());
    }


    public List<Cafe> getCafesByDateAndTimeRange(
            final LocalDate visitDate, final LocalTime startTime, final LocalTime endTime) {

        if(visitDate == null)
            return Arrays.asList();

        if(startTime == null) {
            List<OperationHour> findOperationHours = operationHourRepository.findByDate(visitDate.getDayOfWeek());
            return getCafesByOperationHours(findOperationHours);
        }

        if(endTime == null) {
            List<OperationHour> findOperationHours =
                    operationHourRepository.findByDateAndStartTime(visitDate.getDayOfWeek(), startTime);
            return getCafesByOperationHours(findOperationHours);
        }

        List<OperationHour> findOperationHours =
                operationHourRepository.findOpenHoursByDateAndTimeRange(visitDate.getDayOfWeek(), startTime, endTime);

        return getCafesByOperationHours(findOperationHours);
    }

    private List<Cafe> getCafesByOperationHours(final List<OperationHour> operationHours) {
        return operationHours.stream()
                .map(operationHour -> operationHour.getCafe())
                .collect(Collectors.toList());
    }


    public List<Cafe> getCafesByDistance(
            final List<Cafe> cafes, final BigDecimal latitude,
            final BigDecimal longitude, final Integer withinMinutes) {

        if(latitude == null) return cafes;

        if(withinMinutes == null) {
            return getFilteredCafesByWithinRadius(latitude, longitude, cafes);
        }

        List<Cafe> distanceFilteredCafe = cafes.stream()
                .filter(cafe -> {
                    double walkingTime = calculateWalkingTime(
                            cafe.getLatitude(), cafe.getLongitude(), latitude, longitude);
                    return walkingTime <= withinMinutes;
                })
                .collect(Collectors.toList());

        return distanceFilteredCafe;
    }

    public List<Cafe> getCafesByCafesAndKeywordNames(
            final List<Cafe> cafes, final List<String> keywordNames) {

        List<Keyword> findKeywords = keywordRepository.findByNameIn(keywordNames);
        List<CafeReviewKeyword> findCafeReviewKeywords = cafeReviewKeywordRepository.findByKeywordIn(findKeywords);
        List<Cafe> filteredCafesByKeyword = getCafesByCafeReviewKeywords(findCafeReviewKeywords);

        if(!cafes.isEmpty()) return getEqualCafes(cafes, filteredCafesByKeyword);

        return getCafesByCafeReviewKeywords(findCafeReviewKeywords);
    }

    public List<Cafe> getEqualCafes(List<Cafe> firstCafes, List<Cafe> secondCafes) {
        return firstCafes.stream()
                .filter(secondCafes::contains)
                .collect(Collectors.toList());
    }

    public List<Cafe> getCafesByCafeReviewKeywords(List<CafeReviewKeyword> cafeReviewKeywords) {

        return cafeReviewKeywords .stream()
                .map(cafeReviewKeyword -> cafeReviewKeyword.getCafe())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Cafe> getCafesByKeywordAllMatch(List<Cafe> cafes, List<String> keywords) {

        List<Cafe> cafesByKeywordAllMatch = cafes.stream()
                .filter(cafe -> keywords.stream()
                        .anyMatch(keyword -> keywordRepository.findByCafeId(cafe.getId())
                                .stream()
                                .anyMatch(findKeyword -> findKeyword.getName().equals(keyword))))
                .collect(Collectors.toList());

        return cafesByKeywordAllMatch;
    }

    public List<CafeDto> getCafeDtosByCafes(List<Cafe> cafes){

        return cafes.stream()
                .map(cafe -> CafeDto.of(
                        cafe,
                        cafeImageRepository.findByCafeId(cafe.getId()).get(0).getImageUrl()))
                .collect(Collectors.toList());
    }
}
