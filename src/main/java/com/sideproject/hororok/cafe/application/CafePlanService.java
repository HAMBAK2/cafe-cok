package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.cafe.dto.CreatePlanDto;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.category.dto.CategoryKeywords;
import com.sideproject.hororok.cafe.domain.OperationHour;
import com.sideproject.hororok.cafe.domain.OperationHourRepository;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.plan.enums.PlanMatchType;
import com.sideproject.hororok.utils.converter.FormatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CafePlanService {


    private final OperationHourRepository operationHourRepository;
    private final CafeService cafeService;
    private final CafeRepository cafeRepository;
    private final KeywordService keywordService;

    public CreatePlanDto createPlans(CreatePlanSearchCond searchCond) {
        
        PlanMatchType matchType = PlanMatchType.MATCH;
        List<Cafe> recommendCafes = cafeService.findAllByOrderByStarRatingDescNameAsc();

        //방문시간 기준 필터링
        List<OperationHour> inOperationHoursCafes = dayAndTimeFiltering(searchCond);
        if(inOperationHoursCafes.isEmpty()) {
            matchType = PlanMatchType.MISMATCH;
            return CreatePlanDto.of(matchType, searchCond, FormatConverter.convertVisitDateTime(searchCond), convertCafeListToCafeDtoList(recommendCafes));
        }

        //반경 범위 필터링(근처 카페 추천)
        List<Cafe> distanceFilteredCafes = distanceFiltering(inOperationHoursCafes, searchCond);
        if(distanceFilteredCafes.isEmpty()) {
            matchType = PlanMatchType.MISMATCH;
            return CreatePlanDto.of(matchType, searchCond, FormatConverter.convertVisitDateTime(searchCond),  convertCafeListToCafeDtoList(recommendCafes));
        }

        //3. 카테고리 모두 불일치하는지에 대한 확인
        List<Cafe> keywordFilteredCafes = getKeywordFilteredCafes(searchCond, distanceFilteredCafes);
        if(keywordFilteredCafes.isEmpty()) {
            matchType = PlanMatchType.MISMATCH;
            return CreatePlanDto.of(matchType, searchCond, FormatConverter.convertVisitDateTime(searchCond), convertCafeListToCafeDtoList(recommendCafes));
        }


        //모두 일치하는 것인지 확인
        List<Cafe> allMatchAtKeywordCafes = getAllMatchAtKeywordCafes(keywordFilteredCafes, searchCond);
        if(!allMatchAtKeywordCafes.isEmpty()) {
            orderByDistanceAndStarRating(allMatchAtKeywordCafes, searchCond.getLatitude(), searchCond.getLongitude());
            keywordFilteredCafes.removeAll(allMatchAtKeywordCafes);
            matchType = PlanMatchType.MATCH;

            return CreatePlanDto.of(matchType, searchCond, FormatConverter.convertVisitDateTime(searchCond),
                    convertCafeListToCafeDtoList(allMatchAtKeywordCafes), convertCafeListToCafeDtoList(keywordFilteredCafes));
        }

        matchType = PlanMatchType.SIMILAR;
        
        return CreatePlanDto.of(matchType, searchCond, FormatConverter.convertVisitDateTime(searchCond), convertCafeListToCafeDtoList(keywordFilteredCafes));
    }

    //카드가 모두 일치하는 경우를 찾는다.
    private List<Cafe> getAllMatchAtKeywordCafes(List<Cafe> keywordFilteredCafes, CreatePlanSearchCond searchCond) {

        int idx = 0;
        List<Cafe> allMatchAtKeywordCafes = new ArrayList<>();
        List<String> keywords = new ArrayList<>();

        CategoryKeywords categoryKeywords = searchCond.getCategoryKeywords();
        List<String> atmosphere = categoryKeywords.getAtmosphere();
        List<String> facility = categoryKeywords.getFacility();
        List<String> purpose = categoryKeywords.getPurpose();
        List<String> theme = categoryKeywords.getTheme();
        List<String> menu = categoryKeywords.getMenu();

        if (atmosphere != null) {
            keywords.addAll(atmosphere);
        }
        if (facility != null) {
            keywords.addAll(facility);
        }
        if (purpose != null) {
            keywords.addAll(purpose);
        }
        if (theme != null) {
            keywords.addAll(theme);
        }
        if (menu != null) {
            keywords.addAll(menu);
        }

        for (Cafe keywordFilteredCafe : keywordFilteredCafes) {
            Long cafeId = keywordFilteredCafe.getId();
            List<Keyword> findKeywords = keywordService.findByCafeId(cafeId);

            boolean isAllMatch = keywords.stream()
                    .allMatch(k -> findKeywords.stream()
                            .anyMatch(fk -> fk.getName().equals(k)));

            if(isAllMatch) allMatchAtKeywordCafes.add(keywordFilteredCafe);
        }

        return allMatchAtKeywordCafes;
    }

    //키워드가 완전히 불일치 하는 경우가 있는지 판단한다.
    private List<Cafe> getKeywordFilteredCafes(CreatePlanSearchCond searchCond, List<Cafe> distanceFilteredCafes) {

        List<Cafe> keywordFilteredCafes = new ArrayList<>();
        List<String> keywords = new ArrayList<>();

        CategoryKeywords categoryKeywords = searchCond.getCategoryKeywords();
        List<String> atmosphere = categoryKeywords.getAtmosphere();
        List<String> facility = categoryKeywords.getFacility();
        List<String> purpose = categoryKeywords.getPurpose();
        List<String> theme = categoryKeywords.getTheme();
        List<String> menu = categoryKeywords.getMenu();

        if (atmosphere != null) {
            keywords.addAll(atmosphere);
        }
        if (facility != null) {
            keywords.addAll(facility);
        }
        if (purpose != null) {
            keywords.addAll(purpose);
        }
        if (theme != null) {
            keywords.addAll(theme);
        }
        if (menu != null) {
            keywords.addAll(menu);
        }

        for (Cafe distanceFilteredCafe : distanceFilteredCafes) {
            Long cafeId = distanceFilteredCafe.getId();
            List<Keyword> findKeywords = keywordService.findByCafeId(cafeId);
            boolean isContain = findKeywords.stream()
                    .anyMatch(fw -> keywords.contains(fw.getName()));

            if(isContain) keywordFilteredCafes.add(distanceFilteredCafe);
        }

        return keywordFilteredCafes;
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

        // 정렬
        Collections.sort(targetCafeList, distanceAndRatingComparator);

    }

    
    private List<OperationHour> dayAndTimeFiltering(CreatePlanSearchCond searchCond) {

        String dateString = searchCond.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(dateString, formatter);
        DayOfWeek parseDate = date.getDayOfWeek();

        return operationHourRepository
                .findOpenHoursByDateAndTimeRange(parseDate, searchCond.getStartTime(), searchCond.getEndTime());

    }

    
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


    private List<CafeDto> convertCafeListToCafeDtoList(List<Cafe> cafes) {

        List<CafeDto> cafeDtos = new ArrayList<>();

        for (Cafe cafe : cafes) {
            cafeDtos.add(CafeDto.from(cafe));
        }

        return cafeDtos;
    }


}
