package com.sideproject.hororok.member.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {


    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public MyPageProfileResponse profile(LoginMember loginMember) {

        Long memberId = loginMember.getId();

        Member findMember = memberRepository.getById(memberId);
        Long findReviewCount = reviewRepository.countReviewsByMemberId(memberId);

        return MyPageProfileResponse.of(findMember, findReviewCount);
    }

}
