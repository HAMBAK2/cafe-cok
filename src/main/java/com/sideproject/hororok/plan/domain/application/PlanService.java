package com.sideproject.hororok.plan.domain.application;

import com.sideproject.hororok.cafe.application.CafeService;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.cafe.dto.request.CreatePlanRequest;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.cafe.dto.response.CreatePlanResponse;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.plan.domain.enums.PlanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final CafeRepository cafeRepository;

    private final CafeService cafeService;
    private final KeywordService keywordService;

    public CreatePlanResponse plan(CreatePlanRequest request) {

        List<Cafe> recommendCafes = cafeRepository.findAllByOrderByStarRatingDescNameAsc();
        CategoryKeywordsDto categoryKeywords = keywordService.getCategoryKeywordsByKeywordNames(request.getKeywords());
        CreatePlanResponse misMatchResponse = new CreatePlanResponse(
                        PlanResult.MISMATCH, request, categoryKeywords, CafeDto.fromList(recommendCafes));

        //방문시간 기준 필터링
        List<Cafe> cafesByDateAndTimeRange = cafeService.getCafesByDateAndTimeRange(
                request.getVisitDate(), request.getVisitStartTime(), request.getVisitEndTime());
        if(cafesByDateAndTimeRange.isEmpty()) return misMatchResponse;

        //반경 범위 필터링(근처 카페 추천)
        List<Cafe> cafesByDistance = cafeService.getCafesByDistance(
                cafesByDateAndTimeRange, request.getLatitude(), request.getLongitude(), request.getWithinMinutes());
        if(cafesByDistance.isEmpty()) return misMatchResponse;

        //카테고리 모두 불일치하는지에 대한 확인
        List<Cafe> keywordFilteredCafes = cafeService.getCafesByKeyword(cafesByDistance, request.getKeywords());
        if(keywordFilteredCafes.isEmpty()) return misMatchResponse;

        //모두 일치하는 것인지 확인
        List<Cafe> cafesByKeywordAllMatch = cafeService.getCafesByKeywordAllMatch(keywordFilteredCafes, request.getKeywords());
        if(cafesByKeywordAllMatch.isEmpty()) {
            return new CreatePlanResponse(
                    PlanResult.SIMILAR, request, categoryKeywords,
                    CafeDto.fromList(keywordFilteredCafes));
        }

        orderByDistanceAndStarRating(cafesByKeywordAllMatch, request.getLatitude(), request.getLongitude());
        keywordFilteredCafes.removeAll(cafesByKeywordAllMatch);

        return new CreatePlanResponse(
                PlanResult.MATCH, request, categoryKeywords,
                CafeDto.fromList(cafesByKeywordAllMatch),
                CafeDto.fromList(keywordFilteredCafes));

    }
    
    private void orderByDistanceAndStarRating(List<Cafe> targetCafeList, BigDecimal userLatitude, BigDecimal userLongitude) {

        Comparator<Cafe> distanceAndRatingComparator = new Comparator<Cafe>() {
            @Override
            public int compare(Cafe cafe1, Cafe cafe2) {
                double distance1 = GeometricUtils.calculateDistance(userLatitude, userLongitude, cafe1.getLatitude(), cafe1.getLongitude());
                double distance2 = GeometricUtils.calculateDistance(userLatitude, userLongitude, cafe2.getLatitude(), cafe2.getLongitude());
                int distanceComparison = Double.compare(distance1, distance2);
                if (distanceComparison != 0) {
                    return distanceComparison; // 거리가 다르면 거리 순으로 정렬
                } else {
                    return cafe2.getStarRating().compareTo(cafe1.getStarRating()); // 거리가 같으면 별점 순으로 정렬
                }
            }
        };


        Collections.sort(targetCafeList, distanceAndRatingComparator);
    }



}
