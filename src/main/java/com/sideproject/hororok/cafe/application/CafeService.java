package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.cafe.domain.OperationHour;
import com.sideproject.hororok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.hororok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.hororok.cafe.dto.response.CafeFindAgainResponse;
import com.sideproject.hororok.cafe.dto.response.CafeFindBarResponse;
import com.sideproject.hororok.cafe.dto.response.CafeFindCategoryResponse;
import com.sideproject.hororok.cafe.dto.response.CafeHomeResponse;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.keword.dto.KeywordCount;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.menu.application.MenuService;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.dto.ReviewDetailDto;
import com.sideproject.hororok.review.application.ReviewService;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.cafe.domain.enums.OpenStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final KeywordRepository keywordRepository;
    private final OperationHourRepository operationHourRepository;

    private final MenuService menuService;
    private final ReviewService reviewService;
    private final KeywordService keywordService;
    private final CafeRepository cafeRepository;
    private final CafeImageService cafeImageService;
    private final OperationHourService operationHourService;

    private final BigDecimal MAX_RADIUS = BigDecimal.valueOf(2000);
    
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

    
    public CafeFindCategoryResponse findCafeByCategory(CafeFindCategoryRequest request) {

        List<WithinRadiusCafeDto> withinRadiusCafes = findWithinRadiusCafes(request.getLatitude(), request.getLongitude());
        CategoryKeywordsDto categoryKeywordsDto = keywordService.getAllCategoryKeywords();

        List<String> targetKeywordNames = request.getKeywords();
        List<WithinRadiusCafeDto> filteredWithinRadiusCafes = new ArrayList<>();
        for (WithinRadiusCafeDto withinRadiusCafe : withinRadiusCafes) {
            Cafe cafe = cafeRepository.findById(withinRadiusCafe.getId()).get();
            List<Review> reviews = cafe.getReviews();
            List<String> keywordNames = new ArrayList<>();
            for (Review review : reviews) {
                List<CafeReviewKeyword> cafeReviewKeywords = review.getCafeReviewKeywords();
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

    
    public CafeDetail findCafeDetailByCafeId(Long cafeId){

        Cafe cafe =  cafeRepository.getById(cafeId);
        List<MenuDto> menus = menuService.findByCafeId(cafeId);
        List<String> cafeImageUrls = cafeImageService.findCafeImageUrlsByCafeId(cafeId);
        List<ReviewDetailDto> reviews = reviewService.findReviewByCafeId(cafeId);
        List<String> reviewImageUrls = reviewService.getReviewImageUrlsByCafeId(cafeId);

        //총 6개의 키워드를 뽑아내고 그 중 3개는 카페 키워드로 사용해야 함
        List<KeywordCount> KeywordCounts = keywordService.getUserChoiceKeywordCounts(cafeId);

        addReviewImageUrlsToCafeImageUrls(cafeImageUrls, reviewImageUrls);

        OpenStatus openStatus = operationHourService.getOpenStatus(cafeId);
        List<String> closedDay = operationHourService.getClosedDay(cafeId);
        List<String> businessHours = operationHourService.getBusinessHours(cafeId);

        //유저들이 뽑은 키워드 내용 추가


        return new CafeDetail(
                cafe, menus, openStatus, businessHours, closedDay,
                reviewImageUrls, reviews, KeywordCounts, cafeImageUrls);
    }

    
    public List<WithinRadiusCafeDto> findWithinRadiusCafes(BigDecimal latitude, BigDecimal longitude) {
        List<Cafe> cafes = cafeRepository.findAll();
        List<WithinRadiusCafeDto> withinRadiusCafes = new ArrayList<>();

        for (Cafe cafe : cafes) {
            boolean isWithinRadius = GeometricUtils
                    .isWithinRadius(
                            latitude, longitude,
                            cafe.getLatitude(), cafe.getLongitude(), MAX_RADIUS);

            if(isWithinRadius) {
                String cafeImageUrl =
                        cafeImageService.findOneImageUrlByCafeId(cafe.getId())
                                .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));
                withinRadiusCafes.add(WithinRadiusCafeDto.of(cafe, cafeImageUrl));
            }
        }

        return withinRadiusCafes;
    }


    public List<Cafe> getCafesByDateAndTimeRange(
            LocalDate visitDate, LocalTime startTime, LocalTime endTime) {

        DayOfWeek dayOfWeek = visitDate.getDayOfWeek();
        List<OperationHour> openHoursByDateAndTimeRange =
                operationHourRepository.findOpenHoursByDateAndTimeRange(dayOfWeek, startTime, endTime);

        List<Cafe> cafesByDateAndTimeRange =
                openHoursByDateAndTimeRange.stream()
                        .map(operationHour -> operationHour.getCafe())
                        .collect(Collectors.toList());

        return cafesByDateAndTimeRange;
    }


    public List<Cafe> getCafesByDistance(
            List<Cafe> cafes, BigDecimal latitude, BigDecimal longitude, Integer withinMinutes) {

        List<Cafe> distanceFilteredCafe = cafes.stream()
                .filter(cafe -> {
                    double walkingTime = GeometricUtils.calculateWalkingTime(
                            cafe.getLatitude(), cafe.getLongitude(), latitude, longitude);
                    return walkingTime <= withinMinutes;
                })
                .collect(Collectors.toList());

        return distanceFilteredCafe;
    }

    public List<Cafe> getCafesByKeyword(List<Cafe> cafes, List<String> keywords) {

        List<Cafe> cafesByKeyword = cafes.stream()
                .filter(cafe -> keywordRepository.findByCafeId(cafe.getId()).stream()
                        .anyMatch(keyword -> keywords.contains(keyword.getName())))
                .collect(Collectors.toList());

        return cafesByKeyword;
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
}
