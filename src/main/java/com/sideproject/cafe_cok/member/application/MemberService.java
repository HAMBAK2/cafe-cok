package com.sideproject.cafe_cok.member.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.member.domain.Feedback;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import com.sideproject.cafe_cok.member.domain.repository.FeedbackRepository;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.member.dto.request.MemberFeedbackRequest;
import com.sideproject.cafe_cok.member.dto.response.MemberResponse;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public MemberResponse edit(final LoginMember loginMember,
                               final String nickname,
                               final MultipartFile file) {

        Member findMember = memberRepository.getById(loginMember.getId());
        if(nickname != null) findMember.changeNickname(nickname);
        if(file != null) {
            if(findMember.getPicture() != null) s3Uploader.delete(findMember.getPicture());
            String picture = s3Uploader.upload(file, Constants.MEMBER_ORIGIN_IMAGE_DIR);
            findMember.changePicture(picture);
        }

        return MemberResponse.from(findMember);
    }

    @Transactional
    public void saveFeedback(final LoginMember loginMember,
                             final MemberFeedbackRequest request) {

        Member findMember = memberRepository.getById(loginMember.getId());
        Feedback newFeedback = new Feedback(
                findMember.getEmail(),
                FeedbackCategory.IMPROVEMENT_SUGGESTION,
                request.getContent());
        feedbackRepository.save(newFeedback);
    }

    public MemberResponse find(final LoginMember loginMember) {
        Member findMember = memberRepository.getById(loginMember.getId());
        return MemberResponse.from(findMember);
    }
}
