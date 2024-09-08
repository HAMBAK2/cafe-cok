package com.sideproject.cafe_cok.plan.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.plan.dto.response.PlanResponse;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.PlanCafe;
import com.sideproject.cafe_cok.plan.domain.PlanKeyword;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.domain.repository.PlanCafeRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.plan.dto.request.PlanSaveRequest;
import com.sideproject.cafe_cok.plan.dto.response.SavePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanIdResponse;
import com.sideproject.cafe_cok.util.Constants;
import com.sideproject.cafe_cok.util.ListUtil;
import com.sideproject.cafe_cok.util.tmap.client.TmapClient;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanKeywordException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.util.Constants.*;
import static com.sideproject.cafe_cok.util.FormatConverter.convertLocalDateLocalTimeToString;

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

    @Transactional
    public SavePlanResponse save(final PlanSaveRequest request,
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
            Sort sort = Sort.by(Sort.Order.desc("starRating"));
            findCafes = cafeRepository
                    .findByDateAndTime(request.toCondition(), sort);
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

    private List<Cafe> findCafesWhenDestinationIsPresent(final PlanSaveRequest request) {

        List<Cafe> findCafes = cafeRepository.findByDateAndTimeAndMinutes(request.toCondition());
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
    public PlanIdResponse update(final PlanStatus status,
                                 final Long planId,
                                 final LoginMember loginMember) {

        Plan findPlan = planRepository.getById(planId);
        Member findMember = memberRepository.getById(loginMember.getId());
        if(status.equals(PlanStatus.SAVED)) findPlan.changeIsSaved(true);
        if(status.equals(PlanStatus.SHARED)) findPlan.changeIsShared(true);
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

    public PlanResponse find(final LoginMember loginMember,
                             final Long planId) {

        Plan findPlan = planRepository.getById(planId);
        List<Keyword> findKeywords = keywordRepository.findKeywordByPlanId(planId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);

        List<CafeDto> findSimilarCafes =
                getCafeDtoListByPlanIdAndMatchTypeAndImageType(loginMember.getId(), planId, MatchType.SIMILAR);
        List<CafeDto> findMatchCafes =
                getCafeDtoListByPlanIdAndMatchTypeAndImageType(loginMember.getId(), planId, MatchType.MATCH);

        return new PlanResponse(findPlan, categoryKeywords, findSimilarCafes, findMatchCafes);
    }

    public List<CafeDto> getCafeDtoListByPlanIdAndMatchTypeAndImageType(final Long memberId,
                                                                        final Long planId,
                                                                        final MatchType matchType) {
        List<Cafe> findCafeList = cafeRepository.findByPlanIdAndMatchType(planId, matchType);
        List<CafeDto> cafeDtoList = findCafeList.stream()
                .map(cafe -> {
                    String findImageUrl =
                            imageRepository.getImageByCafeAndImageType(cafe, ImageType.CAFE_MAIN).getThumbnail();

                    List<BookmarkFolderIdsDto> findBookmarkFolderIdsDtoList = null;
                    if (memberId != null) findBookmarkFolderIdsDtoList =
                            bookmarkRepository.getBookmarkFolderIds(cafe.getId(), memberId);
                    return CafeDto.builder()
                            .id(cafe.getId())
                            .name(cafe.getName())
                            .phoneNumber(cafe.getPhoneNumber())
                            .roadAddress(cafe.getRoadAddress())
                            .latitude(cafe.getLatitude())
                            .longitude(cafe.getLongitude())
                            .starRating(cafe.getStarRating())
                            .reviewCount(cafe.getReviewCount())
                            .imageUrl(findImageUrl)
                            .bookmarks(findBookmarkFolderIdsDtoList)
                            .build();
                }).collect(Collectors.toList());
        return cafeDtoList;
    }

    private SavePlanResponse createMisMatchPlan(final PlanSaveRequest request,
                                                final CategoryKeywordsDto categoryKeywords,
                                                final Long memberId) {

        List<Cafe> recommendCafes;
        if(request.getLocationName() != null && !request.getLocationName().isEmpty())
            recommendCafes = cafeRepository.findNearestCafes(request.getLatitude(), request.getLongitude());
        else {
            Sort sort = Sort.by(Sort.Order.desc("starRating"));
            recommendCafes = cafeRepository.findByDate(request.getDate(), sort, 10);
        }

        return SavePlanResponse.builder()
                .matchType(MatchType.MISMATCH)
                .locationName(request.getLocationName())
                .minutes(request.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime()))
                .categoryKeywords(categoryKeywords)
                .recommendCafes(mapToCafeDtoListFromCafeList(recommendCafes, memberId))
                .build();
    }

    private SavePlanResponse createMatchPlan(final PlanSaveRequest request,
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
                    CafeDto newCafeDto = CafeDto.builder()
                            .id(cafe.getId())
                            .name(cafe.getName())
                            .phoneNumber(cafe.getPhoneNumber())
                            .roadAddress(cafe.getRoadAddress())
                            .latitude(cafe.getLatitude())
                            .longitude(cafe.getLongitude())
                            .starRating(cafe.getStarRating())
                            .reviewCount(cafe.getReviewCount())
                            .imageUrl(cafe.getImages().get(0).getThumbnail())
                            .build();
                    if(memberId != null) {
                        newCafeDto.setBookmarks(bookmarkRepository.getBookmarkFolderIds(cafe.getId(), memberId));
                    }
                    return newCafeDto;
                }).collect(Collectors.toList());
    }

    private Long createPlan(final MatchType matchType,
                            final List<Cafe> similarCafes,
                            final List<Cafe> matchCafes,
                            final PlanSaveRequest request) {

        Member findMember = memberRepository.getById(NO_MEMBER_ID);

        Plan plan = request.toEntity(findMember, matchType);
        List<Plan> matchingPlans = planRepository.findMatchingPlan(plan);
        if(!matchingPlans.isEmpty()) {
            Optional<Long> matchingPlanId = matchingPlans.stream()
                    .filter(matchingPlan -> {
                        List<String> findKeywordNames = planKeywordRepository.findByPlanId(matchingPlan.getId())
                                .stream().map(planKeyword -> planKeyword.getKeyword().getName())
                                .collect(Collectors.toList());
                        return ListUtil.areListEqual(request.getKeywords(), findKeywordNames);
                    })
                    .map(Plan::getId)
                    .findFirst();

            if(matchingPlanId.isPresent()) return matchingPlanId.get();
        }

        Plan savedPlan = planRepository.save(plan);
        if(!similarCafes.isEmpty())
            savePlanCafeAllByPlanAndCafeDtosAndMatchType(savedPlan, similarCafes, MatchType.SIMILAR);
        if(!matchCafes.isEmpty())
            savePlanCafeAllByPlanAndCafeDtosAndMatchType(savedPlan, matchCafes, MatchType.MATCH);

        savePlanKeywordAllByPlanAndKeywordNames(savedPlan, request.getKeywords());
        return savedPlan.getId();
    }

    private void savePlanCafeAllByPlanAndCafeDtosAndMatchType(final Plan plan,
                                                              final List<Cafe> cafes,
                                                              final MatchType matchType) {

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
