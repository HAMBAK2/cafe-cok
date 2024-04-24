package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.cafe.dto.request.CafeFindCategoryRequest;
import com.sideproject.hororok.cafe.dto.response.CafeFindAgainResponse;
import com.sideproject.hororok.cafe.dto.response.CafeFindBarResponse;
import com.sideproject.hororok.cafe.dto.response.CafeFindCategoryResponse;
import com.sideproject.hororok.cafe.dto.response.CafeHomeResponse;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.keword.dto.KeywordCount;
import com.sideproject.hororok.menu.dto.MenuInfo;
import com.sideproject.hororok.menu.application.MenuService;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.keword.dto.KeywordInfo;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.dto.ReviewDetail;
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


    public Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new EntityNotFoundException("카페가 존재하지 않습니다."));
    }

    
    public CafeFindAgainResponse findCafeByAgain(BigDecimal latitude, BigDecimal longitude) {

        List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
        CategoryKeywordsDto categoryKeywordsDto = keywordService.getAllCategoryKeywords();

        return CafeFindAgainResponse.of(withinRadiusCafes, categoryKeywordsDto);
    }

    
    public CafeFindBarResponse findCafeByBar(BigDecimal latitude, BigDecimal longitude) {

        Optional<Cafe> findCafe = cafeRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(findCafe.isEmpty()) {
            List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
            CategoryKeywordsDto categoryKeywordsDto = keywordService.getAllCategoryKeywords();
            return CafeFindBarResponse.notExistOf(withinRadiusCafes, categoryKeywordsDto);
        }

        return CafeFindBarResponse
                .existFrom(findCafeDetailByCafeId(findCafe.get().getId()));
    }

    
    public CafeFindCategoryResponse findCafeByCategory(CafeFindCategoryRequest request) {

        List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(request.getLatitude(), request.getLongitude());
        CategoryKeywordsDto categoryKeywordsDto = keywordService.getAllCategoryKeywords();

        List<String> targetKeywordNames = request.getKeywords();
        List<WithinRadiusCafe> filteredWithinRadiusCafes = new ArrayList<>();
        for (WithinRadiusCafe withinRadiusCafe : withinRadiusCafes) {
            Cafe cafe = cafeRepository.findById(withinRadiusCafe.getId()).get();
            List<Review> reviews = cafe.getReviews();
            Set<String> keywordNames = new HashSet<>();
            for (Review review : reviews) {
                List<CafeReviewKeyword> cafeReviewKeywords = review.getCafeReviewKeywords();
                keywordNames
                        .addAll(getKeywordNamesListFromKeywords
                                (keywordService.getKeywordInfosByCafeReviewKeywords(cafeReviewKeywords)));
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

        Cafe cafe =  findCafeById(cafeId);
        List<MenuInfo> menus = menuService.findByCafeId(cafeId);
        List<String> cafeImageUrls = cafeImageService.findCafeImageUrlsByCafeId(cafeId);
        List<ReviewDetail> reviews = reviewService.findReviewByCafeId(cafeId);
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

    
    public List<String> getKeywordNamesListFromKeywords(List<KeywordInfo> keywords) {

        List<String> keywordNames = new ArrayList<>();
        for (KeywordInfo keyword : keywords) {
            keywordNames.add(keyword.getName());
        }

        return keywordNames;
    }
    
    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    
    public List<Cafe> findAllByOrderByStarRatingDescNameAsc() {
        return cafeRepository.findAllByOrderByStarRatingDescNameAsc();
    }
}
