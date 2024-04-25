package com.sideproject.hororok.plan.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.cafe.application.CafeService;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.repository.PlanRepository;
import com.sideproject.hororok.plan.dto.request.CreatePlanRequest;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.plan.dto.request.SavePlanRequest;
import com.sideproject.hororok.plan.dto.request.SharePlanRequest;
import com.sideproject.hororok.plan.dto.response.CreatePlanResponse;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.plan.dto.response.SavePlanResponse;
import com.sideproject.hororok.plan.dto.response.SharePlanResponse;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final CafeRepository cafeRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    private final CafeService cafeService;
    private final KeywordService keywordService;
    private final PlanCafeService planCafeService;
    private final PlanKeywordService planKeywordService;

    private static final Long NO_MEMBER_ID = 1L;

    @Transactional
    public CreatePlanResponse plan(CreatePlanRequest request) {

        List<Cafe> filteredCafes = new ArrayList<>();
        CategoryKeywordsDto categoryKeywords = keywordService.getCategoryKeywordsByKeywordNames(request.getKeywords());

        Boolean isMismatch = isMismatchPlan(request, filteredCafes);
        if(isMismatch) return createMisMatchPlan(request, categoryKeywords);

        List<Cafe> allMatchCafes = new ArrayList<>();
        Boolean isSimilar = isSimilarPlan(request, filteredCafes, allMatchCafes);
        if(isSimilar) return createSimilarPlan(request, categoryKeywords, filteredCafes);

        return createMatchPlan(request, categoryKeywords, filteredCafes, allMatchCafes);
    }

    @Transactional
    public SavePlanResponse save(SavePlanRequest request, LoginMember loginMember) {

        Plan findPlan = planRepository.getById(request.getPlanId());
        Member findMember = memberRepository.getById(loginMember.getId());
        findPlan.setIsSaved(true);
        findPlan.setMember(findMember);
        Plan savedPlan = planRepository.save(findPlan);

        return new SavePlanResponse(savedPlan.getId());
    }

    @Transactional
    public SharePlanResponse share(SharePlanRequest request, LoginMember loginMember) {

        Plan findPlan = planRepository.getById(request.getPlanId());
        Member findMember = memberRepository.getById(loginMember.getId());
        findPlan.setIsShared(true);
        findPlan.setMember(findMember);
        Plan savedPlan = planRepository.save(findPlan);

        return new SharePlanResponse(savedPlan.getId());
    }

    private Boolean isMismatchPlan(CreatePlanRequest request, List<Cafe> filteredCafes) {

        List<Cafe> cafesByDateAndTimeRange = cafeService.getCafesByDateAndTimeRange(
                request.getDate(), request.getStartTime(), request.getEndTime());
        if(cafesByDateAndTimeRange.isEmpty()) return true;

        List<Cafe> cafesByDistance = cafeService.getCafesByDistance(
                cafesByDateAndTimeRange, request.getLatitude(), request.getLongitude(), request.getMinutes());
        if(cafesByDistance.isEmpty()) return true;


        List<Cafe> cafesByKeyword = cafeService.getCafesByKeyword(cafesByDistance, request.getKeywords());
        if(cafesByKeyword.isEmpty()) return true;

        filteredCafes.addAll(cafesByKeyword);

        return false;
    }

    private Boolean isSimilarPlan(CreatePlanRequest request, List<Cafe> filteredCafes, List<Cafe> allMatchCafes) {

        List<Cafe> cafesByKeywordAllMatch = cafeService.getCafesByKeywordAllMatch(filteredCafes, request.getKeywords());
        if(cafesByKeywordAllMatch.isEmpty()) return true;

        allMatchCafes.addAll(cafesByKeywordAllMatch);
        return false;
    }

    private CreatePlanResponse createMisMatchPlan(CreatePlanRequest request, CategoryKeywordsDto categoryKeywords) {

        List<Cafe> recommendCafes = cafeRepository.findAllByOrderByStarRatingDescNameAsc();
        CreatePlanResponse response = new CreatePlanResponse(
                MatchType.MISMATCH, request, categoryKeywords, cafeService.getCafeDtosByCafes(recommendCafes));

        return response;
    }

    private CreatePlanResponse createSimilarPlan(
            CreatePlanRequest request, CategoryKeywordsDto categoryKeywords, List<Cafe> filteredCafes) {

        CreatePlanResponse response =  new CreatePlanResponse(
                MatchType.SIMILAR, request, categoryKeywords,
                cafeService.getCafeDtosByCafes(filteredCafes));

        return createPlan(response, request.getKeywords());
    }

    private CreatePlanResponse createMatchPlan(
            CreatePlanRequest request, CategoryKeywordsDto categoryKeywords,
            List<Cafe> filteredCafes, List<Cafe> allMatchCafes) {

        orderByDistanceAndStarRating(allMatchCafes, request.getLatitude(), request.getLongitude());
        filteredCafes.removeAll(allMatchCafes);

        CreatePlanResponse response = new CreatePlanResponse(
                MatchType.MATCH, request, categoryKeywords,
                cafeService.getCafeDtosByCafes(allMatchCafes),
                cafeService.getCafeDtosByCafes(filteredCafes));

        return createPlan(response, request.getKeywords());
    }

    private CreatePlanResponse createPlan(CreatePlanResponse response, List<String> keywords) {

        Member findMember = memberRepository.getById(NO_MEMBER_ID);

        Plan plan = response.toEntity(findMember);
        Optional<Plan> matchingPlan = planRepository.findMatchingPlan(plan);
        if(matchingPlan.isPresent()) {
            Long planId = matchingPlan.get().getId();
            response.setPlanId(planId);
            return response;
        }

        Plan savedPlan = planRepository.save(plan);
        response.setPlanId(savedPlan.getId());

        List<CafeDto> similarCafes = response.getSimilarCafes();
        planCafeService.saveAll(savedPlan, similarCafes);

        List<CafeDto> matchCafes = response.getMatchCafes();
        if(!matchCafes.isEmpty()) planCafeService.saveAll(savedPlan, matchCafes);

        planKeywordService.saveAll(savedPlan, keywords);

        return response;
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
