package com.sideproject.hororok.member.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.member.dto.MyPagePlanDto;
import com.sideproject.hororok.member.dto.response.MyPagePlanDetailResponse;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import com.sideproject.hororok.member.dto.response.MyPageTagSaveResponse;
import com.sideproject.hororok.plan.application.PlanCafeService;
import com.sideproject.hororok.plan.application.PlanKeywordService;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import com.sideproject.hororok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.hororok.plan.domain.enums.PlanSortBy;
import com.sideproject.hororok.plan.domain.enums.PlanStatus;
import com.sideproject.hororok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.hororok.plan.domain.repository.PlanRepository;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {


    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PlanKeywordRepository planKeywordRepository;

    private final PlanCafeService planCafeService;
    private final PlanKeywordService planKeywordService;
    private final BookmarkFolderService bookmarkFolderService;

    private static final Integer PLAN_MAX_PAGE_SIZE = 4;
    private static final Integer PLAN_DEFAULT_PAGE = 1;

    public MyPageProfileResponse profile(final LoginMember loginMember) {

        Long memberId = loginMember.getId();

        Member findMember = memberRepository.getById(memberId);
        Long findReviewCount = reviewRepository.countReviewsByMemberId(memberId);

        return MyPageProfileResponse.of(findMember, findReviewCount);
    }

    public MyPageTagSaveResponse tagSave(final LoginMember loginMember) {

        List<BookmarkFolderDto> findFolders
                = bookmarkFolderService.getBookmarkFolderDtos(loginMember.getId());

        return MyPageTagSaveResponse.of(findFolders.size(), findFolders);
    }

    public MyPagePlanResponse savedPlan(final LoginMember loginMember, final PlanSortBy sortBy) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans
                    = getPlansByRecent(loginMember, PlanStatus.SAVED, PLAN_DEFAULT_PAGE, PLAN_MAX_PAGE_SIZE);
            case UPCOMING -> plans
                    = getPlansByUpcoming(loginMember, PlanStatus.SAVED, PLAN_DEFAULT_PAGE, PLAN_MAX_PAGE_SIZE);
        }

        return new MyPagePlanResponse(plans);
    }

    public MyPagePlanResponse sharedPlan(final LoginMember loginMember,
                                         final PlanSortBy sortBy) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans
                    = getPlansByRecent(loginMember, PlanStatus.SHARED, PLAN_DEFAULT_PAGE, PLAN_MAX_PAGE_SIZE);
            case UPCOMING -> plans
                    = getPlansByUpcoming(loginMember, PlanStatus.SHARED, PLAN_DEFAULT_PAGE, PLAN_MAX_PAGE_SIZE);
        }

        return new MyPagePlanResponse(plans);
    }

    public MyPagePlanResponse savedPlans(final LoginMember loginMember, final PlanSortBy sortBy,
                                         final Integer page, final Integer size) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlansByRecent(loginMember, PlanStatus.SAVED, page, size);
            case UPCOMING -> plans = getPlansByUpcoming(loginMember, PlanStatus.SAVED, page, size);
        }

        return new MyPagePlanResponse(plans);
    }

    public MyPagePlanResponse sharedPlans(final LoginMember loginMember, final PlanSortBy sortBy,
                                         final Integer page, final Integer size) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlansByRecent(loginMember, PlanStatus.SHARED, page, size);
            case UPCOMING -> plans = getPlansByUpcoming(loginMember, PlanStatus.SHARED, page, size);
        }

        return new MyPagePlanResponse(plans);
    }

    public MyPagePlanDetailResponse planDetail(Long planId) {

        Plan findPlan = planRepository.getById(planId);
        List<Keyword> findKeywords = planKeywordService.getKeywordsByPlanId(planId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);
        List<CafeDto> findSimilarCafes
                = planCafeService.getCafeDtosByPlanIdAndMatchType(planId, PlanCafeMatchType.SIMILAR);

        if(findPlan.getMatchType().equals(MatchType.MATCH)) {
            List<CafeDto> findMatchCafes
                    = planCafeService.getCafeDtosByPlanIdAndMatchType(planId, PlanCafeMatchType.MATCH);
            return MyPagePlanDetailResponse.of(findPlan, categoryKeywords, findSimilarCafes, findMatchCafes);
        }

        return MyPagePlanDetailResponse.of(findPlan, categoryKeywords, findSimilarCafes);
    }

    private List<MyPagePlanDto> getPlansByRecent(final LoginMember loginMember,
                                                 final PlanStatus planStatus,
                                                 final Integer page, final Integer size) {

        PageRequest pageRequest =
                PageRequest.of(page-1, size, by(Direction.DESC, PlanSortBy.RECENT.getValue()));

        Page<Plan> findPlanPage = planRepository.findPageByMemberId(loginMember.getId(), pageRequest);

        List<MyPagePlanDto> plans = new ArrayList<>();
        for (Plan plan : findPlanPage) {
            if(planStatus.equals(PlanStatus.SAVED) && !plan.getIsSaved()) continue;
            if(planStatus.equals(PlanStatus.SHARED) && !plan.getIsShared()) continue;
            if(!plan.getIsSaved()) continue;
            KeywordDto findKeywordDto = KeywordDto
                    .from(planKeywordRepository
                            .getFirstByPlanIdAndKeywordCategory(plan.getId(), Category.PURPOSE)
                            .getKeyword());

            plans.add(MyPagePlanDto.of(plan, findKeywordDto));
        }

        return plans;
    }

    private List<MyPagePlanDto> getPlansByUpcoming(final LoginMember loginMember,
                                                   final PlanStatus planStatus,
                                                   final Integer page, final Integer size) {

        PageRequest pageRequest =
                PageRequest.of(page-1, size, by(Direction.ASC, PlanSortBy.UPCOMING.getValue())
                        .and(by(Direction.ASC, "visitStartTime")));

        Page<Plan> findPlanPage = planRepository.findPageByMemberIdAndUpcomingPlanCondition(
                loginMember.getId(), LocalDate.now(), LocalTime.now(), pageRequest);

        List<MyPagePlanDto> plans = new ArrayList<>();
        for (Plan plan : findPlanPage) {
            if(planStatus.equals(PlanStatus.SAVED) && !plan.getIsSaved()) continue;
            if(planStatus.equals(PlanStatus.SHARED) && !plan.getIsShared()) continue;

            KeywordDto findKeywordDto = KeywordDto
                    .from(planKeywordRepository
                            .getFirstByPlanIdAndKeywordCategory(plan.getId(), Category.PURPOSE)
                            .getKeyword());

            plans.add(MyPagePlanDto.of(plan, findKeywordDto));
        }

        return plans;
    }
}
