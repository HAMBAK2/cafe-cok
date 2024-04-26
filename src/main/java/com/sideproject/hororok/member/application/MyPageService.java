package com.sideproject.hororok.member.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.member.dto.MyPagePlanDto;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import com.sideproject.hororok.member.dto.response.MyPageTagSaveResponse;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.enums.PlanSortBy;
import com.sideproject.hororok.plan.domain.enums.PlanStatus;
import com.sideproject.hororok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.hororok.plan.domain.repository.PlanRepository;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


    private final BookmarkFolderService bookmarkFolderService;

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


    public MyPagePlanResponse savedPlan(final LoginMember loginMember,
                                        final PlanSortBy sortBy,
                                        final Integer count) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlanByRecent(loginMember, PlanStatus.SAVED, count);
            case UPCOMING -> plans = getPlanByUpcoming(loginMember, PlanStatus.SAVED, count);
        }

        return new MyPagePlanResponse(plans);
    }

    public MyPagePlanResponse sharedPlan(final LoginMember loginMember,
                                        final PlanSortBy sortBy,
                                        final Integer count) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlanByRecent(loginMember, PlanStatus.SHARED, count);
            case UPCOMING -> plans = getPlanByUpcoming(loginMember, PlanStatus.SHARED, count);
        }

        return new MyPagePlanResponse(plans);
    }

    private List<MyPagePlanDto> getPlanByRecent(final LoginMember loginMember,
                                                     final PlanStatus planStatus,
                                                     final Integer count) {

        PageRequest pageRequest =
                PageRequest.of(0, count, by(Direction.DESC, PlanSortBy.RECENT.getValue()));

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

    private List<MyPagePlanDto> getPlanByUpcoming(final LoginMember loginMember,
                                                       final PlanStatus planStatus,
                                                       final Integer count) {

        PageRequest pageRequest =
                PageRequest.of(0, count, by(Direction.ASC, PlanSortBy.UPCOMING.getValue())
                        .and(by(Direction.ASC, "visitStartTime")));

        Page<Plan> findPlanPage = planRepository
                .findPageByMemberIdAndVisitDateGreaterThanEqualAndVisitStartTimeAfter(
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
