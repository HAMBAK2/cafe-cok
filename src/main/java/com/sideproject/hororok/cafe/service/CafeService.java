package com.sideproject.hororok.cafe.service;

import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.menu.service.MenuService;
import com.sideproject.hororok.cafe.cond.CafeCategorySearchCond;
import com.sideproject.hororok.cafe.cond.CafeSearchCond;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.cafe.repository.CafeRepository;
import com.sideproject.hororok.cafeImage.service.CafeImageService;
import com.sideproject.hororok.category.service.CategoryService;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.operationHours.dto.BusinessScheduleDto;
import com.sideproject.hororok.operationHours.service.OperationHourService;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.review.service.ReviewService;
import com.sideproject.hororok.reviewImage.service.ReviewImageService;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final ReviewImageService reviewImageService;
    private final CafeImageService cafeImageService;
    private final ReviewService reviewService;
    private final CafeRepository cafeRepository;
    private final MenuService menuService;
    private final CategoryService categoryService;
    private final OperationHourService operationHourService;

    private final BigDecimal MAX_RADIUS = BigDecimal.valueOf(2000);

    @Transactional
    public CafeDetailDto findCafeDetail(Long cafeId){

        Cafe cafe =  findCafeById(cafeId);
        List<MenuDto> menus = menuService.findByCafeId(cafeId);
        List<String> reviewImageUrls = reviewImageService.findReviewImageUrlsByCafeId(cafeId);
        List<String> cafeImageUrls = cafeImageService.findCafeImageUrlsByCafeId(cafeId);
        List<ReviewDto> reviews = reviewService.findReviewByCafeId(cafeId);

        //리뷰중에서 태그의 개수가 많은 거 3개 뽑아야함
        List<KeywordDto> cafeKeywords = reviewService.findKeywordInReviewByCafeIdOrderByDesc(cafe.getId());
        addReviewImageUrlsToCafeImageUrls(cafeImageUrls, reviewImageUrls);

        //운영과 휴무시간
        BusinessScheduleDto businessSchedule = operationHourService.getWorkTimeInfo(cafeId);


        return CafeDetailDto.of(cafe, menus, reviewImageUrls, reviews,
                cafeKeywords, cafeImageUrls,businessSchedule);
    }


    private void addReviewImageUrlsToCafeImageUrls(List<String> cafeImageUrls, List<String> reviewImageUrls){

        int idx = 0;
        while(idx < reviewImageUrls.size() && idx < 2) {
            cafeImageUrls.add(reviewImageUrls.get(idx));
            idx++;
        }
    }

    public Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new EntityNotFoundException("카페가 존재하지 않습니다."));
    }

    public Cafe findByLongitudeAndLatitude(CafeSearchCond cafeSearchCond) {
        return cafeRepository.findByLongitudeAndLatitude(cafeSearchCond.getLongitude(), cafeSearchCond.getLatitude())
                .orElseThrow(() -> new EntityNotFoundException("카페가 존재하지 않습니다."));
    }

    public CafeReSearchDto findWithinRadius(CafeSearchCond searchCond) {
        List<Cafe> cafes = findAll();
        List<Cafe> withinRadiusCafes = new ArrayList<>();
        boolean isExist = false;
        for (Cafe cafe : cafes) {
            boolean withinRadius = GeometricUtils.isWithinRadius(searchCond.getLatitude(), searchCond.getLongitude(),
                    cafe.getLatitude(), cafe.getLongitude(), MAX_RADIUS);

            if(withinRadius) {
                withinRadiusCafes.add(cafe);
                isExist = true;
            }
        }



        return CafeReSearchDto.of(isExist, withinRadiusCafes, categoryService.findAllCategoryAndKeyword());
    }


    public CafeBarSearchDto barSearch(CafeSearchCond searchCond) {

        if (!cafeRepository.existsByLongitudeAndLatitude(searchCond.getLongitude(), searchCond.getLatitude())) {
            CafeReSearchDto withinRadius = findWithinRadius(searchCond);
            return CafeBarSearchDto.from(withinRadius);
        }

        return CafeBarSearchDto
                .from(findCafeDetail(cafeRepository
                        .findByLongitudeAndLatitude(searchCond.getLongitude(), searchCond.getLatitude()).get().getId()));
    }

    public CafeCategorySearchDto categorySearch(CafeCategorySearchCond searchCond) {
        CafeReSearchDto withinRadius = findWithinRadius(CafeSearchCond.from(searchCond));

        List<Cafe> cafeWithKeywordsInReview = reviewService.findCafeWithKeywordsInReview(searchCond);
        List<Cafe> cafes = withinRadius.getCafes();

        List<Cafe> sameCafes = cafeWithKeywordsInReview.stream()
                .map(Cafe::getId)
                .collect(Collectors.toSet())
                .stream()
                .flatMap(cafeId ->
                        cafes.stream()
                                .filter(cafe -> cafe.getId().equals(cafeId)))
                .collect(Collectors.toList());

        return CafeCategorySearchDto.builder()
                .keywordsByCategory(withinRadius.getKeywordsByCategory())
                .cafes(sameCafes)
                .build();
    }



    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    public List<Cafe> findAllByOrderByStarRatingDescNameAsc() {
        return cafeRepository.findAllByOrderByStarRatingDescNameAsc();
    }
}
