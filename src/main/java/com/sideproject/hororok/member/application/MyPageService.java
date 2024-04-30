package com.sideproject.hororok.member.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.cafe.domain.repository.CafeImageRepository;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.combination.domain.Combination;
import com.sideproject.hororok.combination.domain.repository.CombinationRepository;
import com.sideproject.hororok.combination.dto.CombinationDto;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.member.dto.MyPagePlanDto;
import com.sideproject.hororok.member.dto.response.*;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.PlanCafe;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import com.sideproject.hororok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.hororok.plan.domain.enums.PlanSortBy;
import com.sideproject.hororok.plan.domain.enums.PlanStatus;
import com.sideproject.hororok.plan.domain.repository.PlanCafeRepository;
import com.sideproject.hororok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.hororok.plan.domain.repository.PlanRepository;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import com.sideproject.hororok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static com.sideproject.hororok.utils.Constants.*;
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
    private final CafeImageRepository cafeImageRepository;
    private final PlanKeywordRepository planKeywordRepository;
    private final CombinationRepository combinationRepository;

    public MyPageProfileResponse profile(final LoginMember loginMember) {

        Long memberId = loginMember.getId();

        Member findMember = memberRepository.getById(memberId);
        Long findReviewCount = reviewRepository.countReviewsByMemberId(memberId);

        return MyPageProfileResponse.of(findMember, findReviewCount);
    }

    @Transactional
    public MyPageProfileEditResponse editProfile
            (final LoginMember loginMember, final String nickname, final MultipartFile file) throws IOException {

        Member findMember = memberRepository.getById(loginMember.getId());
        if(nickname != null) findMember.changeNickname(nickname);

        if(file != null) {
            if(findMember.getPicture() != null) s3Uploader.delete(findMember.getPicture());
            String picture = s3Uploader.upload(file, MEMBER_IMAGE_DIR).replace(IMAGE_URL_PREFIX, "");
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

    public MyPagePlansResponse sharedPlans(final LoginMember loginMember, final PlanSortBy sortBy,
                                         final Integer page, final Integer size) {

        List<MyPagePlanDto> plans = new ArrayList<>();
        switch (sortBy){
            case RECENT -> plans = getPlansByRecent(loginMember, PlanStatus.SHARED, page, size);
            case UPCOMING -> plans = getPlansByUpcoming(loginMember, PlanStatus.SHARED, page, size);
        }

        return new MyPagePlansResponse(page, plans);
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
                        .and(by(Direction.ASC, "visitStartTime"))
                        .and(by(Direction.ASC, "id")));

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
