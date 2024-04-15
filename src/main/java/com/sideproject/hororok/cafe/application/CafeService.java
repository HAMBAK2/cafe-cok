package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.hororok.cafe.dto.response.CafeFindAgainResponse;
import com.sideproject.hororok.cafe.dto.response.CafeFindBarResponse;
import com.sideproject.hororok.cafe.dto.response.CafeFindCategoryResponse;
import com.sideproject.hororok.category.dto.CategoryKeywords;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.menu.application.MenuService;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.category.application.CategoryService;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.review.application.ReviewService;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.cafe.domain.OpenStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final MenuService menuService;
    private final ReviewService reviewService;
    private final CafeRepository cafeRepository;
    private final CategoryService categoryService;
    private final CafeImageService cafeImageService;
    private final OperationHourService operationHourService;

    private final BigDecimal MAX_RADIUS = BigDecimal.valueOf(2000);

    @LogTrace
    private void addReviewImageUrlsToCafeImageUrls(List<String> cafeImageUrls, List<String> reviewImageUrls){

        int idx = 0;
        while(idx < reviewImageUrls.size() && idx < 2) {
            cafeImageUrls.add(reviewImageUrls.get(idx));
            idx++;
        }
    }

    @LogTrace
    public Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new EntityNotFoundException("카페가 존재하지 않습니다."));
    }

    @LogTrace
    public CafeFindAgainResponse findCafeByAgain(BigDecimal latitude, BigDecimal longitude) {

        List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
        CategoryKeywords categoryKeywords = categoryService.findAllCategoryAndKeyword();

        return CafeFindAgainResponse.of(withinRadiusCafes, categoryKeywords);
    }

    @LogTrace
    public CafeFindBarResponse findCafeByBar(BigDecimal latitude, BigDecimal longitude) {

        Optional<Cafe> findCafe = cafeRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(findCafe.isEmpty()) {
            List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
            CategoryKeywords categoryKeywords = categoryService.findAllCategoryAndKeyword();
            return CafeFindBarResponse.notExistOf(withinRadiusCafes, categoryKeywords);
        }

        return CafeFindBarResponse
                .existFrom(findCafeDetailByCafeId(findCafe.get().getId()));
    }

    @LogTrace
    public CafeFindCategoryResponse findCafeByCategory(CafeFindCategoryRequest request) {

        List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(request.getLatitude(), request.getLongitude());
        CategoryKeywords categoryKeywords = categoryService.findAllCategoryAndKeyword();

        List<String> targetKeywordNames = getKeywordNamesListFromCategoryKeywords(request.getCategoryKeywords());
        List<WithinRadiusCafe> filteredWithinRadiusCafes = new ArrayList<>();
        for (WithinRadiusCafe withinRadiusCafe : withinRadiusCafes) {
            Cafe cafe = cafeRepository.findById(withinRadiusCafe.getId()).get();
            List<Review> reviews = cafe.getReviews();
            Set<String> keywordNames = new HashSet<>();
            for (Review review : reviews) {
                keywordNames.addAll(getKeywordNamesListFromKeywords(review.getKeywords()));
            }

            boolean allMatch = targetKeywordNames.stream()
                    .allMatch(keywordNames::contains);

            if(allMatch) filteredWithinRadiusCafes.add(withinRadiusCafe);
        }

        return CafeFindCategoryResponse.builder()
                .categoryKeywords(categoryKeywords)
                .cafes(filteredWithinRadiusCafes)
                .build();
    }

    @LogTrace
    public CafeDetail findCafeDetailByCafeId(Long cafeId){

        Cafe cafe =  findCafeById(cafeId);
        List<MenuDto> menus = menuService.findByCafeId(cafeId);
        List<String> cafeImageUrls = cafeImageService.findCafeImageUrlsByCafeId(cafeId);
        List<ReviewDto> reviews = reviewService.findReviewByCafeId(cafeId);
        List<String> reviewImageUrls = reviewService.getReviewImageUrlsByCafeId(cafeId);

        //리뷰중에서 태그의 개수가 많은 거 3개 뽑아야함
        List<KeywordDto> cafeKeywords = reviewService.findKeywordInReviewByCafeIdOrderByDesc(cafe.getId());
        addReviewImageUrlsToCafeImageUrls(cafeImageUrls, reviewImageUrls);

        OpenStatus openStatus = operationHourService.getOpenStatus(cafeId);
        List<String> closedDay = operationHourService.getClosedDay(cafeId);
        List<String> businessHours = operationHourService.getBusinessHours(cafeId);

        //유저들이 뽑은 키워드 내용 추가


        return new CafeDetail(
                cafe, menus, openStatus, businessHours, closedDay,
                reviewImageUrls, reviews, cafeKeywords, cafeImageUrls);
    }


    @LogTrace
    public List<WithinRadiusCafe> findWithinRadiusCafes(BigDecimal latitude, BigDecimal longitude) {
        List<Cafe> cafes = findAll();
        List<WithinRadiusCafe> withinRadiusCafes = new ArrayList<>();

        for (Cafe cafe : cafes) {
            boolean isWithinRadius = GeometricUtils
                    .isWithinRadius(
                            latitude, longitude,
                            cafe.getLatitude(), cafe.getLongitude(), MAX_RADIUS);

            if(isWithinRadius) {
                String cafeImageUrl =
                        cafeImageService.findOneImageUrlByCafeId(cafe.getId())
                                .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));
                withinRadiusCafes.add(WithinRadiusCafe.of(cafe, cafeImageUrl));
            }
        }

        return withinRadiusCafes;
    }

    @LogTrace
    public List<String> getKeywordNamesListFromKeywords(List<Keyword> keywords) {

        List<String> keywordNames = new ArrayList<>();
        for (Keyword keyword : keywords) {
            keywordNames.add(keyword.getName());
        }

        return keywordNames;
    }

    @LogTrace
    public List<String> getKeywordNamesListFromCategoryKeywords(CategoryKeywords categoryKeywords) {

        List<String> keywordNames = new ArrayList<>();
        List<String> atmosphere = categoryKeywords.getAtmosphere();
        List<String> facility = categoryKeywords.getFacility();
        List<String> purpose = categoryKeywords.getPurpose();
        List<String> theme = categoryKeywords.getTheme();
        List<String> menu = categoryKeywords.getMenu();
        if (atmosphere != null) {
            keywordNames.addAll(atmosphere);
        }
        if (facility != null) {
            keywordNames.addAll(facility);
        }
        if (purpose != null) {
            keywordNames.addAll(purpose);
        }
        if (theme != null) {
            keywordNames.addAll(theme);
        }
        if (menu != null) {
            keywordNames.addAll(menu);
        }

        return keywordNames;
    }

    @LogTrace
    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    @LogTrace
    public List<Cafe> findAllByOrderByStarRatingDescNameAsc() {
        return cafeRepository.findAllByOrderByStarRatingDescNameAsc();
    }
}
