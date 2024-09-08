package com.sideproject.cafe_cok.member.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.member.domain.Feedback;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import com.sideproject.cafe_cok.member.domain.repository.FeedbackRepository;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.member.dto.request.MemberFeedbackRequest;
import com.sideproject.cafe_cok.member.dto.response.MemberResponse;
import com.sideproject.cafe_cok.plan.domain.condition.PlanSearchCondition;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import com.sideproject.cafe_cok.member.dto.response.MemberPlansResponse;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanSortException;
import com.sideproject.cafe_cok.util.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sideproject.cafe_cok.util.Constants.*;
import static org.springframework.data.domain.Sort.by;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    private final PlanRepository planRepository;

    @Transactional
    public MemberResponse update(final LoginMember loginMember,
                                 final String nickname,
                                 final MultipartFile file) {

        Member findMember = memberRepository.getById(loginMember.getId());
        String updateNickname = findMember.getNickname();
        String updatePicture = findMember.getPicture();

        if(nickname != null) updateNickname = nickname;
        if(file != null) {
            if(findMember.getPicture() != null) s3Uploader.delete(findMember.getPicture());
            updatePicture = s3Uploader.upload(file, MEMBER_ORIGIN_IMAGE_DIR);
        }

        memberRepository.update(findMember.getId(), updateNickname, updatePicture);
        return new MemberResponse(findMember);
    }

    @Transactional
    public MemberResponse saveFeedback(final LoginMember loginMember,
                             final MemberFeedbackRequest request) {

        Member findMember = memberRepository.getById(loginMember.getId());
        Feedback newFeedback = Feedback.builder()
                .email(findMember.getEmail())
                .category(FeedbackCategory.IMPROVEMENT_SUGGESTION)
                .content(request.getContent())
                .build();

        feedbackRepository.save(newFeedback);
        return new MemberResponse(findMember);
    }

    public MemberResponse find(final LoginMember loginMember) {
        Member findMember = memberRepository.getById(loginMember.getId());
        return new MemberResponse(findMember);
    }

    public MemberPlansResponse findPlanList(final LoginMember loginMember,
                                            final PlanSortBy planSortBy,
                                            final PlanStatus status) {

        PlanSearchCondition planSearchCondition
                = new PlanSearchCondition(loginMember.getId(), Category.PURPOSE, planSortBy, status);
        Sort sort = getSort(planSortBy);
        Pageable pageable = PageRequest.of(FIRST_PAGE_NUMBER, MAX_PAGE_SIZE, sort);
        List<PlanKeywordDto> plans = planRepository.findPlanKeywords(planSearchCondition, pageable);

        return new MemberPlansResponse(plans);
    }

    private Sort getSort(final PlanSortBy planSortBy) {
        Sort sort;
        switch (planSortBy){
            case RECENT -> sort = by(Sort.Direction.DESC, PlanSortBy.RECENT.getValue());
            case UPCOMING -> sort = by(Sort.Direction.ASC, PlanSortBy.UPCOMING.getValue(), "visitStartTime", "id");
            default -> throw new NoSuchPlanSortException();
        }

        return sort;
    }
}
