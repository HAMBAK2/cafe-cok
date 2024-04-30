package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.cafe.domain.OperationHour;
import com.sideproject.hororok.cafe.domain.repository.CafeImageRepository;
import com.sideproject.hororok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.hororok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.hororok.cafe.dto.response.*;
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
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.cafe.domain.enums.OpenStatus;
import com.sideproject.hororok.review.domain.repository.ReviewImageRepository;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import com.sideproject.hororok.review.dto.CafeDetailReviewDto;
import com.sideproject.hororok.utils.FormatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final KeywordRepository keywordRepository;
    private final CafeImageRepository cafeImageRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final OperationHourRepository operationHourRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    public CafeHomeResponse home() {
        List<Keyword> keywords = keywordRepository.findAll();
        return new CafeHomeResponse(new CategoryKeywordsDto(keywords));
    }

    
    public CafeFindAgainResponse findCafeByAgain(BigDecimal latitude, BigDecimal longitude) {

        List<CafeDto> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
        CategoryKeywordsDto categoryKeywordsDto = new CategoryKeywordsDto(keywordRepository.findAll());

        return CafeFindAgainResponse.of(withinRadiusCafes, categoryKeywordsDto);
    }

    
    public CafeFindBarResponse findCafeByBar(BigDecimal latitude, BigDecimal longitude) {

        Optional<Cafe> findCafe = cafeRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(findCafe.isEmpty()) {
            List<CafeDto> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
            CategoryKeywordsDto categoryKeywordsDto = new CategoryKeywordsDto(keywordRepository.findAll());
            return CafeFindBarResponse.notExistOf(withinRadiusCafes, categoryKeywordsDto);
        }

        return CafeFindBarResponse
                .existFrom(detailTop(findCafe.get().getId()), detailBasicInfo(findCafe.get().getId()));
    }

    
    public CafeFindCategoryResponse findCafeByKeyword(CafeFindCategoryRequest request) {

        List<CafeDto> withinRadiusCafes = findWithinRadiusCafes(request.getLatitude(), request.getLongitude());
        CategoryKeywordsDto categoryKeywordsDto = new CategoryKeywordsDto(keywordRepository.findAll());

        List<String> targetKeywordNames = request.getKeywords();
        List<CafeDto> filteredWithinRadiusCafes = new ArrayList<>();
        for (CafeDto withinRadiusCafe : withinRadiusCafes) {
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

        return CafeFindCategoryResponse.of(filteredWithinRadiusCafes, categoryKeywordsDto);
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
        List<String> imageUrls = getImageUrlsByCafeIdAndReviewImageCnt(cafeId, CAFE_DETAIL_BASIC_INFO_IMAGE_MAX_CNT);
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtosByCafeId(cafeId);

        return CafeDetailBasicInfoResponse
                .of(findCafe, openStatus, businessHours, closedDay, menus,
                        imageUrls, userChoiceKeywords, reviews);
    }

    public CafeDetailMenuResponse detailMenus(final Long cafeId) {

        List<MenuDto> menuDtos = MenuDto.fromList(menuRepository.findByCafeId(cafeId));
        return CafeDetailMenuResponse.from(menuDtos);
    }

    public CafeDetailImageResponse detailImages(final Long cafeId) {
        List<String> imageUrls = getImageUrlsByCafeIdAndReviewImageCnt(cafeId, CAFE_DETAIL_IMAGE_REVIEW_IMG_CNT);
        return CafeDetailImageResponse.from(imageUrls);
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
            List<String> imageUrls = reviewImageRepository.findImageUrlsByCafeIdOrderByCreatedDateDesc(cafeId,
                            PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_IMG_CNT));
            cafeDetailReviewDtos.add(CafeDetailReviewDto.of(review, imageUrls, keywordDtos));
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

    private List<String> getImageUrlsByCafeIdAndReviewImageCnt(final Long cafeId, final Integer reviewImageCnt) {

        List<String> combinedImageUrls = new ArrayList<>();

        List<String> cafeImageUrls = cafeImageRepository
                        .findImageUrlsByCafeId(cafeId, PageRequest.of(0, CAFE_DETAIL_IMAGE_MAX_CNT));

        List<String> reviewImageUrls = reviewImageRepository
                .findImageUrlsByCafeIdOrderByCreatedDateDesc(cafeId,
                        PageRequest.of(0, reviewImageCnt - cafeImageUrls.size()));

        combinedImageUrls.addAll(cafeImageUrls);
        combinedImageUrls.addAll(reviewImageUrls);

        return combinedImageUrls;

    }

    public List<CafeDto> findWithinRadiusCafes(BigDecimal latitude, BigDecimal longitude) {
        List<Cafe> cafes = cafeRepository.findAll();
        List<CafeDto> withinRadiusCafes = new ArrayList<>();

        for (Cafe cafe : cafes) {
            boolean isWithinRadius = isWithinRadius(
                            latitude, longitude,
                            cafe.getLatitude(), cafe.getLongitude());

            if(isWithinRadius) {
                String cafeImageUrl = cafeImageRepository.getOneImageUrlByCafeId(cafe.getId());
                withinRadiusCafes.add(CafeDto.of(cafe, cafeImageUrl));
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
