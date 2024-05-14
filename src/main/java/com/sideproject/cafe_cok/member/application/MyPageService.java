package com.sideproject.cafe_cok.member.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationRepository;
import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.member.dto.MyPagePlanDto;
import com.sideproject.cafe_cok.member.dto.response.*;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.PlanCafe;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.domain.repository.PlanCafeRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.dto.response.*;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static org.springframework.data.domain.Sort.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final S3Uploader s3Uploader;

    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final KeywordRepository keywordRepository;
    private final PlanCafeRepository planCafeRepository;
    private final PlanKeywordRepository planKeywordRepository;
    private final CombinationRepository combinationRepository;

    private final Integer PLANS_ALL_PAGE = -1;
    private final Integer PLANS_ALL_SIZE = -1;

    public MyPageProfileResponse profile(final LoginMember loginMember) {

        Long memberId = loginMember.getId();

        Member findMember = memberRepository.getById(memberId);
        Long findReviewCount = reviewRepository.countReviewsByMemberId(memberId);

        return MyPageProfileResponse.of(findMember, findReviewCount);
    }

    @Transactional
    public MyPageProfileEditResponse editProfile
            (final LoginMember loginMember, final String nickname, final MultipartFile file) {

        Member findMember = memberRepository.getById(loginMember.getId());
        if(nickname != null) findMember.changeNickname(nickname);

        if(file != null) {
            if(findMember.getPicture() != null) s3Uploader.delete(findMember.getPicture());
            String picture = s3Uploader.upload(file, Constants.MEMBER_ORIGIN_IMAGE_DIR);
            findMember.changePicture(picture);
        }

        Member savedMember = memberRepository.save(findMember);
        return new MyPageProfileEditResponse(savedMember.getNickname(), savedMember.getPicture());
    }

    public MyPagePlansResponse savedPlans(final LoginMember loginMember, final PlanSortBy sortBy,
                                          final Integer page, final Integer size) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlansByRecent(loginMember, PlanStatus.SAVED, page, size);
            case UPCOMING -> plans = getPlansByUpcoming(loginMember, PlanStatus.SAVED, page, size);
        }

        return new MyPagePlansResponse(page, plans);
    }

    public MyPagePlansAllResponse savedPlansAll(final LoginMember loginMember, final PlanSortBy sortBy) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlansByRecent(loginMember, PlanStatus.SAVED, PLANS_ALL_PAGE, PLANS_ALL_SIZE);
            case UPCOMING -> plans = getPlansByUpcoming(loginMember, PlanStatus.SAVED, PLANS_ALL_PAGE, PLANS_ALL_SIZE);
        }

        return MyPagePlansAllResponse.from(plans);
    }

    public MyPagePlansResponse sharedPlans(final LoginMember loginMember, final PlanSortBy sortBy,
                                           final Integer page, final Integer size) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlansByRecent(loginMember, PlanStatus.SHARED, page, size);
            case UPCOMING -> plans = getPlansByUpcoming(loginMember, PlanStatus.SHARED, page, size);
        }

        return new MyPagePlansResponse(page, plans);
    }

    public MyPagePlansAllResponse sharedPlansAll(final LoginMember loginMember, final PlanSortBy sortBy) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlansByRecent(loginMember, PlanStatus.SHARED, PLANS_ALL_PAGE, PLANS_ALL_SIZE);
            case UPCOMING -> plans = getPlansByUpcoming(loginMember, PlanStatus.SHARED, PLANS_ALL_PAGE, PLANS_ALL_SIZE);
        }

        return MyPagePlansAllResponse.from(plans);
    }

    public MyPagePlanDetailResponse planDetail(Long planId) {

        Plan findPlan = planRepository.getById(planId);
        List<Keyword> findKeywords = keywordRepository.findKeywordByPlanId(planId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);
        List<CafeDto> findSimilarCafes = getCafeDtosByPlanIdAndMatchType(planId, PlanCafeMatchType.SIMILAR);

        if(findPlan.getMatchType().equals(MatchType.MATCH)) {
            List<CafeDto> findMatchCafes = getCafeDtosByPlanIdAndMatchType(planId, PlanCafeMatchType.MATCH);
            return MyPagePlanDetailResponse.of(findPlan, categoryKeywords, findSimilarCafes, findMatchCafes);
        }

        return MyPagePlanDetailResponse.of(findPlan, categoryKeywords, findSimilarCafes);
    }

    public MyPageCombinationResponse combination(final LoginMember loginMember) {

        List<Combination> findCombination = combinationRepository.findByMemberId(loginMember.getId());
        if(findCombination.isEmpty()) return MyPageCombinationResponse.builder().build();

        List<CombinationDto> combinations = CombinationDto.fromList(findCombination);
        return MyPageCombinationResponse.from(combinations);
    }

    private List<CafeDto> getCafeDtosByPlanIdAndMatchType(Long planId, PlanCafeMatchType matchType) {
        List<PlanCafe> findPlanCafes = planCafeRepository.findByPlanIdAndMatchType(planId, matchType);
        return findPlanCafes.stream()
                .map(planCafe -> CafeDto.from(planCafe.getCafe()))
                .collect(Collectors.toList());
    }

    private List<MyPagePlanDto> getPlansByRecent(final LoginMember loginMember,
                                                 final PlanStatus planStatus,
                                                 final Integer page, final Integer size) {

        PageRequest pageRequest = null;
        if(page == PLANS_ALL_PAGE && size == PLANS_ALL_SIZE)
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE, by(Direction.DESC, PlanSortBy.RECENT.getValue()));
        else pageRequest = PageRequest.of(page-1, size, by(Direction.DESC, PlanSortBy.RECENT.getValue()));
        Page<Plan> findPlanPage = planRepository.findPageByMemberId(loginMember.getId(), pageRequest);

        List<MyPagePlanDto> plans = new ArrayList<>();
        for (Plan plan : findPlanPage) {
            if(planStatus.equals(PlanStatus.SAVED) && !plan.getIsSaved()) continue;
            if(planStatus.equals(PlanStatus.SHARED) && !plan.getIsShared()) continue;
            if(!plan.getIsSaved()) continue;

            Keyword keyword = planKeywordRepository
                    .getFirstByPlanIdAndKeywordCategory(plan.getId(), Category.PURPOSE)
                    .getKeyword();

            plans.add(MyPagePlanDto.of(plan, keyword.getName()));
        }

        return plans;
    }

    private List<MyPagePlanDto> getPlansByUpcoming(final LoginMember loginMember,
                                                   final PlanStatus planStatus,
                                                   final Integer page, final Integer size) {
        PageRequest pageRequest = null;
        if(page == PLANS_ALL_PAGE && size == PLANS_ALL_SIZE){
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE, by(Direction.ASC, PlanSortBy.UPCOMING.getValue())
                    .and(by(Direction.ASC, "visitStartTime"))
                    .and(by(Direction.ASC, "id")));
        }else {
            pageRequest = PageRequest.of(page-1, size, by(Direction.ASC, PlanSortBy.UPCOMING.getValue())
                    .and(by(Direction.ASC, "visitStartTime"))
                    .and(by(Direction.ASC, "id")));
        }

        Page<Plan> findPlanPage = planRepository.findPageByMemberIdAndUpcomingPlanCondition(
                loginMember.getId(), LocalDate.now(), LocalTime.now(), pageRequest);

        List<MyPagePlanDto> plans = new ArrayList<>();
        for (Plan plan : findPlanPage) {
            if(planStatus.equals(PlanStatus.SAVED) && !plan.getIsSaved()) continue;
            if(planStatus.equals(PlanStatus.SHARED) && !plan.getIsShared()) continue;

            Keyword keyword = planKeywordRepository
                    .getFirstByPlanIdAndKeywordCategory(plan.getId(), Category.PURPOSE)
                    .getKeyword();

            plans.add(MyPagePlanDto.of(plan, keyword.getName()));
        }

        return plans;
    }
}
