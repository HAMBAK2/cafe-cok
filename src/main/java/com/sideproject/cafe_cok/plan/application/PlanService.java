package com.sideproject.cafe_cok.plan.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.PlanCafe;
import com.sideproject.cafe_cok.plan.domain.PlanKeyword;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.domain.repository.PlanCafeRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SavePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SharePlanRequest;
import com.sideproject.cafe_cok.plan.dto.response.CreatePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.DeletePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.SavePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.SharePlanResponse;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.ListUtils;
import com.sideproject.cafe_cok.utils.tmap.client.TmapClient;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanKeywordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.utils.FormatConverter.convertLocalDateLocalTimeToString;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final TmapClient tmapClient;

    private final CafeRepository cafeRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final PlanCafeRepository planCafeRepository;
    private final PlanKeywordRepository planKeywordRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public CreatePlanResponse plan(final CreatePlanRequest request,
                                   final Long memberId) {

        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(keywordRepository.findByNameIn(request.getKeywords()));
        if(categoryKeywords.getPurpose().isEmpty()) throw new NoSuchPlanKeywordException();

        List<Cafe> notMismatchCafes = getNotMissMatchCafes(request);
        if(notMismatchCafes.isEmpty()) return createMisMatchPlan(request, categoryKeywords, memberId);

        List<Cafe> allMatchCafes = getCafesByKeywordAllMatch(notMismatchCafes, request.getKeywords());
        if(allMatchCafes.isEmpty()) createSimilarPlan(request, categoryKeywords, notMismatchCafes, memberId);
        return createMatchPlan(request, categoryKeywords, notMismatchCafes, allMatchCafes, memberId);
    }

    @Transactional
    public SavePlanResponse save(final SavePlanRequest request,
                                 final LoginMember loginMember) {

        Plan findPlan = planRepository.getById(request.getPlanId());
        Member findMember = memberRepository.getById(loginMember.getId());
        findPlan.changeIsSaved(true);
        findPlan.changeMember(findMember);
        return new SavePlanResponse(findPlan.getId());
    }

    @Transactional
    public SharePlanResponse share(final SharePlanRequest request,
                                   final LoginMember loginMember) {

        Plan findPlan = planRepository.getById(request.getPlanId());
        Member findMember = memberRepository.getById(loginMember.getId());
        findPlan.changeIsShared(true);
        findPlan.changeMember(findMember);
        return new SharePlanResponse(findPlan.getId());
    }

    @Transactional
    public DeletePlanResponse delete(final PlanStatus status,
                                     final Long planId) {

        Plan findPlan = planRepository.getById(planId);
        if(status.equals(PlanStatus.SAVED)) findPlan.changeIsSaved(false);
        else findPlan.changeIsShared(false);

        if(!findPlan.getIsSaved() && !findPlan.getIsShared()) planRepository.delete(findPlan);
        return new DeletePlanResponse(findPlan.getId());
    }


    private List<Cafe> getNotMissMatchCafes(final CreatePlanRequest request) {

        List<Cafe> notMismatchCafes = cafeRepository.findNotMismatchCafes(request);
        Map<Cafe, Integer> walkingTimeMap = new HashMap<>();

        return notMismatchCafes.stream().filter(cafe -> {
                    int walkingTime = walkingTimeMap.computeIfAbsent(cafe, key -> tmapClient.getWalkingTime(
                            request.getLongitude(), request.getLatitude(),
                            key.getLongitude(), key.getLatitude()));
                    return walkingTime <= request.getMinutes();
                })
                .sorted(Comparator.comparingInt((Cafe cafe) -> walkingTimeMap.get(cafe))
                        .thenComparing(Comparator.comparingDouble((Cafe cafe)
                                -> cafe.getStarRating().doubleValue()).reversed()))
                .collect(Collectors.toList());
    }

    private List<Cafe> getCafesByKeywordAllMatch(final List<Cafe> cafes,
                                                 final List<String> keywords) {

        List<Cafe> cafesByKeywordAllMatch = cafes.stream()
                .filter(cafe -> keywords.stream()
                        .allMatch(keyword -> keywordRepository.findByCafeId(cafe.getId())
                                .stream()
                                .anyMatch(findKeyword -> findKeyword.getName().equals(keyword))))
                .collect(Collectors.toList());
        return cafesByKeywordAllMatch;
    }

    private CreatePlanResponse createMisMatchPlan(final CreatePlanRequest request,
                                                  final CategoryKeywordsDto categoryKeywords,
                                                  final Long memberId) {

        List<Cafe> recommendCafes = cafeRepository.findNearestCafes(request.getLatitude(), request.getLongitude());
        return CreatePlanResponse.builder()
                .matchType(MatchType.MISMATCH)
                .locationName(request.getLocationName())
                .minutes(request.getMinutes() == 0 ? 30 : request.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime()))
                .categoryKeywords(categoryKeywords)
                .recommendCafes(mapToCafeDtoListFromCafeList(recommendCafes, memberId))
                .build();
    }

    private CreatePlanResponse createSimilarPlan(final CreatePlanRequest request,
                                                 final CategoryKeywordsDto categoryKeywords,
                                                 final List<Cafe> similarCafes,
                                                 final Long memberId) {

        MatchType matchType = MatchType.SIMILAR;
        Long planId = createPlan(matchType, similarCafes, Collections.emptyList(), request);

        return CreatePlanResponse.builder()
                .planId(planId)
                .matchType(matchType)
                .locationName(request.getLocationName())
                .minutes(request.getMinutes() == 0 ? 30 : request.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime()))
                .categoryKeywords(categoryKeywords)
                .similarCafes(mapToCafeDtoListFromCafeList(similarCafes, memberId))
                .build();
    }

    private CreatePlanResponse createMatchPlan(final CreatePlanRequest request,
                                               final CategoryKeywordsDto categoryKeywords,
                                               final List<Cafe> notMismatchCafes,
                                               final List<Cafe> allMatchCafes,
                                               final Long memberId) {

        List<Cafe> similarCafes = new ArrayList<>(notMismatchCafes);
        similarCafes.removeAll(allMatchCafes);
        MatchType matchType = MatchType.MATCH;
        Long planId = createPlan(matchType, similarCafes, allMatchCafes, request);

        return CreatePlanResponse.builder()
                .planId(planId)
                .matchType(matchType)
                .locationName(request.getLocationName())
                .minutes(request.getMinutes() == 0 ? 30 : request.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime()))
                .categoryKeywords(categoryKeywords)
                .similarCafes(mapToCafeDtoListFromCafeList(similarCafes, memberId))
                .matchCafes(mapToCafeDtoListFromCafeList(allMatchCafes, memberId))
                .build();
    }

    private List<CafeDto> mapToCafeDtoListFromCafeList(final List<Cafe> cafeList,
                                                       final Long memberId) {
        return cafeList.stream()
                .map(cafe -> {
                    CafeDto newCafeDto = new CafeDto(cafe, cafe.getImages().get(0).getThumbnail());
                    if(memberId != null) {
                        newCafeDto.setBookmarks(bookmarkRepository
                                .findBookmarkIdDtoListByCafeIdAndMemberId(cafe.getId(), memberId));
                    }
                    return newCafeDto;
                }).collect(Collectors.toList());
    }

    private Long createPlan(final MatchType matchType,
                            final List<Cafe> similarCafes,
                            final List<Cafe> matchCafes,
                            final CreatePlanRequest request) {

        Member findMember = memberRepository.getById(Constants.NO_MEMBER_ID);

        Plan plan = request.toEntity(findMember, matchType);
        List<Plan> matchingPlans = planRepository.findMatchingPlan(plan);
        if(!matchingPlans.isEmpty()) {
            Optional<Long> matchingPlanId = matchingPlans.stream()
                    .filter(matchingPlan -> {
                        List<String> findKeywordNames = planKeywordRepository.findByPlanId(matchingPlan.getId())
                                .stream().map(planKeyword -> planKeyword.getKeyword().getName())
                                .collect(Collectors.toList());
                        return ListUtils.areListEqual(request.getKeywords(), findKeywordNames);
                    })
                    .map(Plan::getId)
                    .findFirst();

            if(matchingPlanId.isPresent()) return matchingPlanId.get();
        }

        Plan savedPlan = planRepository.save(plan);
        if(!similarCafes.isEmpty())
            savePlanCafeAllByPlanAndCafeDtosAndMatchType(savedPlan, similarCafes, PlanCafeMatchType.SIMILAR);
        if(!matchCafes.isEmpty())
            savePlanCafeAllByPlanAndCafeDtosAndMatchType(savedPlan, matchCafes, PlanCafeMatchType.MATCH);

        savePlanKeywordAllByPlanAndKeywordNames(savedPlan, request.getKeywords());
        return savedPlan.getId();
    }

    private void savePlanCafeAllByPlanAndCafeDtosAndMatchType(final Plan plan,
                                                              final List<Cafe> cafes,
                                                              final PlanCafeMatchType matchType) {

        List<PlanCafe> planCafes = cafes.stream()
                .map(cafe -> new PlanCafe(plan, cafe, matchType))
                .collect(Collectors.toList());
        planCafeRepository.saveAll(planCafes);
    }

    private void savePlanKeywordAllByPlanAndKeywordNames(final Plan plan,
                                                         final List<String> keywordNames) {

        List<Keyword> findKeywords = keywordRepository.findByNameIn(keywordNames);
        List<PlanKeyword> planKeywords = findKeywords.stream()
                .map(keyword -> new PlanKeyword(plan, keyword))
                .collect(Collectors.toList());
        planKeywordRepository.saveAll(planKeywords);
    }
}
