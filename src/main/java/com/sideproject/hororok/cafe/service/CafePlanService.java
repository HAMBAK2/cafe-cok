package com.sideproject.hororok.cafe.service;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.dto.CreatePlanDto;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.cafe.repository.CafeRepository;
import com.sideproject.hororok.category.dto.CategoryAndKeyword;
import com.sideproject.hororok.category.service.CategoryService;
import com.sideproject.hororok.operationHours.entity.OperationHour;
import com.sideproject.hororok.operationHours.repository.OperationHourRepository;
import com.sideproject.hororok.review.service.ReviewService;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.plan.enums.PlanMatchType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CafePlanService {


    private final OperationHourRepository operationHourRepository;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final CafeService cafeService;
    private final CafeRepository cafeRepository;

    @LogTrace
    public CreatePlanDto createPlans(CreatePlanSearchCond searchCond) {
        
        PlanMatchType matchType = PlanMatchType.MATCH;
        List<Cafe> recommendCafes = cafeService.findAllByOrderByStarRatingDescNameAsc();


        //방문시간 기준 필터링
        List<OperationHour> inOperationHoursCafes = dayAndTimeFiltering(searchCond);
        if(inOperationHoursCafes.isEmpty()) {
            matchType = PlanMatchType.MISMATCH;
            return CreatePlanDto.of(matchType, searchCond, recommendCafes);
        }

        //반경 범위 필터링(근처 카페 추천)
        List<Cafe> distanceFilteredCafes = distanceFiltering(inOperationHoursCafes, searchCond);
        if(distanceFilteredCafes.isEmpty()) {
            matchType = PlanMatchType.MISMATCH;
            return CreatePlanDto.of(matchType, searchCond, recommendCafes);
        }

        //3. 카테고리 모두 불일치하는지에 대한 확인
        List<Cafe> keywordFilteredCafes = getKeywordFilteredCafes(searchCond, distanceFilteredCafes);
        if(keywordFilteredCafes.isEmpty()) {
            matchType = PlanMatchType.MISMATCH;
            return CreatePlanDto.of(matchType, searchCond, recommendCafes);
        }


        //모두 일치하는 것인지 확인
        List<Cafe> allMatchAtKeywordCafes = getAllMatchAtKeywordCafes(keywordFilteredCafes, searchCond);
        if(!allMatchAtKeywordCafes.isEmpty()) {
            orderByDistanceAndStarRating(allMatchAtKeywordCafes, searchCond.getLatitude(), searchCond.getLongitude());
            keywordFilteredCafes.removeAll(allMatchAtKeywordCafes);
            matchType = PlanMatchType.MATCH;
            return CreatePlanDto.of(matchType, searchCond, allMatchAtKeywordCafes, keywordFilteredCafes);
        }

        matchType = PlanMatchType.SIMILAR;
        return CreatePlanDto.of(matchType, searchCond, keywordFilteredCafes);
    }

    @LogTrace
    //카드가 모두 일치하는 경우를 찾는다.
    private List<Cafe> getAllMatchAtKeywordCafes(List<Cafe> keywordFilteredCafes, CreatePlanSearchCond searchCond) {

        int idx = 0;
        List<Cafe> allMatchAtKeywordCafes = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        for (CategoryAndKeyword keyword : searchCond.getKeywords()) {

            List<String> keywordByCategory = keyword.getKeywords();
            for (String s : keywordByCategory) { keywords.add(s);}
        }

        for (Cafe keywordFilteredCafe : keywordFilteredCafes) {
            List<String> findKeywords = cafeRepository.findKeywordsByReviewsCafeId(keywordFilteredCafe.getId());

            if(keywords.size() > findKeywords.size()) continue;

            boolean isAllMatch = keywords.stream().allMatch(findKeywords::contains);
            if(isAllMatch) allMatchAtKeywordCafes.add(keywordFilteredCafe);
        }

        return allMatchAtKeywordCafes;

    }

    @LogTrace
    //키워드가 완전히 불일치 하는 경우가 있는지 판단한다.
    private List<Cafe> getKeywordFilteredCafes(CreatePlanSearchCond searchCond, List<Cafe> distanceFilteredCafes) {

        List<Cafe> keywordFilteredCafes = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        for (CategoryAndKeyword keyword : searchCond.getKeywords()) {

            List<String> keywordByCategory = keyword.getKeywords();
            for (String s : keywordByCategory) { keywords.add(s);}
        }

        for (Cafe distanceFilteredCafe : distanceFilteredCafes) {
            Long cafeId = distanceFilteredCafe.getId();
            Optional<Cafe> distinctByKeywordsAndCafeId = cafeRepository.findDistinctByKeywordsAndCafeId(keywords, cafeId);
            if(distinctByKeywordsAndCafeId.isEmpty()) continue;
            keywordFilteredCafes.add(distanceFilteredCafe);
        }

        return keywordFilteredCafes;
    }

    @LogTrace
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

        // 정렬
        Collections.sort(targetCafeList, distanceAndRatingComparator);

    }

    @LogTrace
    private List<OperationHour> dayAndTimeFiltering(CreatePlanSearchCond searchCond) {

        String dateString = searchCond.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            DayOfWeek parseDate = date.getDayOfWeek();

            return operationHourRepository
                    .findOpenHoursByDateAndTimeRange(parseDate, searchCond.getStartTime(), searchCond.getEndTime());
        } catch (DateTimeParseException e) {
            // 날짜 형식이 잘못되었을 때 처리할 내용
            e.printStackTrace();
            return Collections.emptyList(); // 예시로 비어있는 리스트 반환
        }

    }

    @LogTrace
    private List<Cafe> distanceFiltering(List<OperationHour> inOperationHoursCafes, CreatePlanSearchCond searchCond) {

        List<Cafe> distanceFilteredCafe = new ArrayList<>();

        for (OperationHour inOperationHoursCafe : inOperationHoursCafes) {
            Cafe cafe = inOperationHoursCafe.getCafe();
            double walkingTime = GeometricUtils.calculateWalkingTime(cafe.getLatitude(), cafe.getLongitude(),
                    searchCond.getLatitude(), searchCond.getLongitude());

            if (walkingTime <= searchCond.getMinutes()) {
                distanceFilteredCafe.add(cafe);
            }
        }

        return distanceFilteredCafe;
    }
}
