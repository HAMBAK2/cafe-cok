package com.sideproject.hororok.member.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import com.sideproject.hororok.member.dto.response.MemberMyPageResponse;
import com.sideproject.hororok.review.domain.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BookmarkFolderService bookmarkFolderService;


    public MemberMyPageResponse myPage(LoginMember loginMember) {

        Long memberId = loginMember.getId();

        Member findMember = memberRepository.getById(memberId);
        Long findReviewCount = reviewRepository.countReviewsByMemberId(memberId);
        List<BookmarkFolderDto> findFolders
                = bookmarkFolderService.getBookmarkFolderDtos(memberId);

        return new MemberMyPageResponse(findMember, findReviewCount, findFolders);
    }
}
