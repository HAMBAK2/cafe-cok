package com.sideproject.cafe_cok.plan.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.plan.dto.response.PlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanAllResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanPageResponse;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.PlanCafe;
import com.sideproject.cafe_cok.plan.domain.PlanKeyword;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.domain.repository.PlanCafeRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SavePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SharePlanRequest;
import com.sideproject.cafe_cok.plan.dto.response.SavePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanIdResponse;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanSortException;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.ListUtils;
import com.sideproject.cafe_cok.utils.tmap.client.TmapClient;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanKeywordException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.utils.FormatConverter.convertLocalDateLocalTimeToString;
import static org.springframework.data.domain.Sort.by;


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
    private final ImageRepository imageRepository;

    private final Integer FIRST_PAGE_NUMBER = 0;
    private final Integer MAX_PAGE_SIZE = Integer.MAX_VALUE;

    @Transactional
    public SavePlanResponse doPlan(final CreatePlanRequest request,
                                   final Long memberId) {

        request.validate();
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(keywordRepository.findByNameIn(request.getKeywords()));
        if(categoryKeywords.getPurpose().isEmpty()) throw new NoSuchPlanKeywordException();

        List<Cafe> findCafes;
        List<Cafe> matchCafes;
        List<Cafe> similarCafes = new ArrayList<>();
        if(request.getLocationName() != null && !request.getLocationName().isEmpty()) {
            findCafes = findCafesWhenDestinationIsPresent(request);
            matchCafes = findCafes.stream()
                    .filter(cafe -> {
                        List<String> cafeKeywordNames = cafe.getCafeReviewKeywords().stream()
                                .map(cafeReviewKeyword -> cafeReviewKeyword.getKeyword().getName())
                                .collect(Collectors.toList());
                        boolean allMatch = request.getKeywords().stream().allMatch(cafeKeywordNames::contains);
                        if(cafeKeywordNames.isEmpty()) allMatch = false;
                        if (!allMatch ) {
                            similarCafes.add(cafe);
                            return false;
                        }
                        return true;
                    }).collect(Collectors.toList());
        }
        else {
            findCafes = cafeRepository.findByDateAndTimeOrderByStarRatingDesc(request);
            List<String> findPurposeNames = keywordRepository.findKeywordNamesByCategory(request.getKeywords(), Category.PURPOSE);
            matchCafes = findCafes.stream()
                    .filter(cafe -> {

                        List<String> cafePurposeNames = new ArrayList<>();
                        List<String> cafeKeywordNames = cafe.getCafeReviewKeywords().stream()
                                .map(cafeReviewKeyword -> {
                                    Keyword keyword = cafeReviewKeyword.getKeyword();
                                    if(keyword.getCategory().equals(Category.PURPOSE))
                                        cafePurposeNames.add(keyword.getName());
                                    return keyword.getName();
                                })
                                .collect(Collectors.toList());

                        if(cafeKeywordNames.isEmpty()) return false;
                        boolean allMatch = request.getKeywords().stream().allMatch(cafeKeywordNames::contains);
                        if (!allMatch) {
                            if(findPurposeNames.stream().allMatch(cafePurposeNames::contains)) similarCafes.add(cafe);
                            return false;
                        }
                        return true;
                    }).collect(Collectors.toList());
        }

        if(matchCafes.isEmpty() && similarCafes.isEmpty()) return createMisMatchPlan(request, categoryKeywords, memberId);
        return createMatchPlan(request, categoryKeywords, similarCafes, matchCafes, memberId);
    }

    private List<Cafe> findCafesWhenDestinationIsPresent(final CreatePlanRequest request) {

        List<Cafe> findCafes = cafeRepository.findByDateAndTimeAndDistance(request);
        Map<Cafe, Integer> walkingTimeMap = new HashMap<>();

        return findCafes.stream()
                .filter(cafe -> {
                    int walkTime = walkingTimeMap.computeIfAbsent(cafe, key -> tmapClient
                            .getWalkingTime(request.getLongitude(), request.getLatitude(),
                                    key.getLongitude(), key.getLatitude()));
                    return walkTime <= request.getMinutes();
                })
                .sorted(Comparator.comparingInt((Cafe cafe) -> walkingTimeMap.get(cafe))
                        .thenComparing(Comparator.comparingDouble((Cafe cafe)
                                -> cafe.getStarRating().doubleValue()).reversed()))
                .collect(Collectors.toList());
    }


    @Transactional
    public PlanIdResponse save(final SavePlanRequest request,
                                 final LoginMember loginMember) {

        Plan findPlan = planRepository.getById(request.getPlanId());
        Member findMember = memberRepository.getById(loginMember.getId());
        findPlan.changeIsSaved(true);
        findPlan.changeMember(findMember);
        return new PlanIdResponse(findPlan.getId());
    }

    @Transactional
    public PlanIdResponse share(final SharePlanRequest request,
                                final LoginMember loginMember) {

        Plan findPlan = planRepository.getById(request.getPlanId());
        Member findMember = memberRepository.getById(loginMember.getId());
        findPlan.changeIsShared(true);
        findPlan.changeMember(findMember);
        return new PlanIdResponse(findPlan.getId());
    }

    @Transactional
    public PlanIdResponse delete(final PlanStatus status,
                                     final Long planId) {

        Plan findPlan = planRepository.getById(planId);
        if(status.equals(PlanStatus.SAVED)) findPlan.changeIsSaved(false);
        else findPlan.changeIsShared(false);

        if(!findPlan.getIsSaved() && !findPlan.getIsShared()) planRepository.delete(findPlan);
        return new PlanIdResponse(findPlan.getId());
    }

    public PlanPageResponse getPlans(final LoginMember loginMember,
                                     final PlanSortBy planSortBy,
                                     final PlanStatus status,
                                     final Integer page,
                                     final Integer size) {

        Sort sortBy = getSortBy(planSortBy);
        PageRequest pageRequest = PageRequest.of(page-1, size, sortBy);
        List<PlanKeywordDto> plans = planRepository
                .findPlansByMemberIdAndCategory(loginMember.getId(), Category.PURPOSE, planSortBy, status, pageRequest);

        return new PlanPageResponse(page, plans);
    }

    public PlanAllResponse getPlansAll(final LoginMember loginMember,
                                       final PlanSortBy planSortBy,
                                       final PlanStatus status) {

        Sort sortBy = getSortBy(planSortBy);
        PageRequest pageRequest = PageRequest.of(FIRST_PAGE_NUMBER, MAX_PAGE_SIZE, sortBy);
        List<PlanKeywordDto> plans = planRepository
                .findPlansByMemberIdAndCategory(loginMember.getId(), Category.PURPOSE, planSortBy, status, pageRequest);

        return new PlanAllResponse(plans);
    }

    public PlanResponse findPlan(final LoginMember loginMember,
                                 final Long planId) {

        Plan findPlan = planRepository.getById(planId);
        List<Keyword> findKeywords = keywordRepository.findKeywordByPlanId(planId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);

        List<CafeDto> findSimilarCafes =
                getCafeDtoListByPlanIdAndMatchTypeAndImageType(loginMember.getId(), planId, PlanCafeMatchType.SIMILAR);
        List<CafeDto> findMatchCafes =
                getCafeDtoListByPlanIdAndMatchTypeAndImageType(loginMember.getId(), planId, PlanCafeMatchType.MATCH);

        return new PlanResponse(findPlan, categoryKeywords, findSimilarCafes, findMatchCafes);
    }

    public List<CafeDto> getCafeDtoListByPlanIdAndMatchTypeAndImageType(final Long memberId,
                                                                        final Long planId,
                                                                        final PlanCafeMatchType matchType) {
        List<Cafe> findCafeList = cafeRepository.findByPlanIdAndMatchType(planId, matchType);
        List<CafeDto> cafeDtoList = findCafeList.stream()
                .map(cafe -> {
                    String findImageUrl =
                            imageRepository.getImageByCafeAndImageType(cafe, ImageType.CAFE_MAIN).getThumbnail();

                    List<BookmarkIdDto> findBookmarkIdDtoList = null;
                    if (memberId != null) findBookmarkIdDtoList =
                            bookmarkRepository.findBookmarkIdDtoListByCafeIdAndMemberId(cafe.getId(), memberId);
                    return new CafeDto(cafe, findImageUrl, findBookmarkIdDtoList);
                }).collect(Collectors.toList());
        return cafeDtoList;
    }

    private Sort getSortBy(final PlanSortBy planSortBy) {
        Sort sortBy;
        switch (planSortBy){
            case RECENT -> sortBy = by(Sort.Direction.DESC, PlanSortBy.RECENT.getValue());
            case UPCOMING -> sortBy = by(Sort.Direction.ASC, PlanSortBy.UPCOMING.getValue(), "visitStartTime", "id");
            default -> throw new NoSuchPlanSortException();
        }

        return sortBy;
    }

    private SavePlanResponse createMisMatchPlan(final CreatePlanRequest request,
                                                final CategoryKeywordsDto categoryKeywords,
                                                final Long memberId) {

        List<Cafe> recommendCafes;
        if(request.getLocationName() != null && !request.getLocationName().isEmpty())
            recommendCafes = cafeRepository.findNearestCafes(request.getLatitude(), request.getLongitude());
        else recommendCafes = cafeRepository.findDateAndLimit(request.getDate(), 10);

        return SavePlanResponse.builder()
                .matchType(MatchType.MISMATCH)
                .locationName(request.getLocationName())
                .minutes(request.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime()))
                .categoryKeywords(categoryKeywords)
                .recommendCafes(mapToCafeDtoListFromCafeList(recommendCafes, memberId))
                .build();
    }

    private SavePlanResponse createMatchPlan(final CreatePlanRequest request,
                                             final CategoryKeywordsDto categoryKeywords,
                                             final List<Cafe> similarCafes,
                                             final List<Cafe> matchCafes,
                                             final Long memberId) {

        MatchType matchType = MatchType.SIMILAR;
        if(!matchCafes.isEmpty()) matchType = MatchType.MATCH;
        Long planId = createPlan(matchType, similarCafes, matchCafes, request);

        return SavePlanResponse.builder()
                .planId(planId)
                .matchType(matchType)
                .locationName(request.getLocationName())
                .minutes(request.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime()))
                .categoryKeywords(categoryKeywords)
                .similarCafes(mapToCafeDtoListFromCafeList(similarCafes, memberId))
                .matchCafes(mapToCafeDtoListFromCafeList(matchCafes, memberId))
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
