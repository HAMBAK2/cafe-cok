package com.sideproject.hororok.member.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.member.dto.response.MyPagePlanDetailResponse;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageResponse;
import com.sideproject.hororok.plan.application.PlanCafeService;
import com.sideproject.hororok.plan.application.PlanKeywordService;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import com.sideproject.hororok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.hororok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.hororok.plan.domain.repository.PlanRepository;
import com.sideproject.hororok.plan.dto.PlanDto;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PlanRepository planRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PlanKeywordRepository planKeywordRepository;

    private final PlanKeywordService planKeywordService;
    private final BookmarkFolderService bookmarkFolderService;
    private final PlanCafeService planCafeService;

    private static final Integer MY_PAGE_PLAN_MAX_CNT = 5;

    public MyPageResponse myPage(LoginMember loginMember) {

        Long memberId = loginMember.getId();

        Member findMember = memberRepository.getById(memberId);
        Long findReviewCount = reviewRepository.countReviewsByMemberId(memberId);
        List<BookmarkFolderDto> findFolders
                = bookmarkFolderService.getBookmarkFolderDtos(memberId);

        return new MyPageResponse(findMember, findReviewCount, findFolders);
    }

    public MyPagePlanResponse plan(LoginMember loginMember) {

        List<Plan> findPlans = planRepository.findByMemberIdOrderByCreatedDateDesc(loginMember.getId());

        List<PlanDto> savedPlanDtos = getSavedPlanDtos(findPlans);
        List<PlanDto> sharedPlanDtos = getSharedPlanDtos(findPlans);

        return MyPagePlanResponse.from(savedPlanDtos, sharedPlanDtos);
    }

    public MyPagePlanDetailResponse planDetail(Long planId) {

        Plan findPlan = planRepository.getById(planId);
        List<Keyword> findKeywords = planKeywordService.getKeywordsByPlanId(planId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);
        List<CafeDto> findSimilarCafes = planCafeService.getCafeDtosByPlanIdAndMatchType(planId, PlanCafeMatchType.SIMILAR);

        if(findPlan.getMatchType().equals(MatchType.MATCH)) {
            List<CafeDto> findMatchCafes = planCafeService.getCafeDtosByPlanIdAndMatchType(planId, PlanCafeMatchType.MATCH);
            MyPagePlanDetailResponse.of(findPlan, categoryKeywords, findSimilarCafes, findMatchCafes);
        }

        return MyPagePlanDetailResponse.of(findPlan, categoryKeywords, findSimilarCafes);
    }

    private List<PlanDto> getSavedPlanDtos(List<Plan> findPlans) {
        return findPlans.stream()
                .filter(findPlan -> findPlan.getIsSaved())
                .map(findPlan -> {
                    String findKeyword = planKeywordRepository
                            .getFirstByPlanIdAndKeywordCategory(findPlan.getId(), Category.PURPOSE)
                            .getKeyword().getName();
                    return PlanDto.of(findPlan, findKeyword);
                })
                .limit(MY_PAGE_PLAN_MAX_CNT)
                .collect(Collectors.toList());
    }


    private List<PlanDto> getSharedPlanDtos(List<Plan> findPlans) {
        return findPlans.stream()
                .filter(findPlan -> findPlan.getIsShared())
                .map(findPlan -> {
                    String findKeyword = planKeywordRepository
                            .getFirstByPlanIdAndKeywordCategory(findPlan.getId(), Category.PURPOSE)
                            .getKeyword().getName();
                    return PlanDto.of(findPlan, findKeyword);
                })
                .limit(MY_PAGE_PLAN_MAX_CNT)
                .collect(Collectors.toList());
    }
}
