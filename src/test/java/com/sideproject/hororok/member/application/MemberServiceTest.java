package com.sideproject.hororok.member.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import com.sideproject.hororok.member.dto.response.MemberMyPageResponse;
import com.sideproject.hororok.review.domain.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.일반_폴더_DTO;
import static com.sideproject.hororok.common.fixtures.LoginMemberFixtures.로그인_맴버;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰_개수;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookmarkFolderService bookmarkFolderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("myPage 메서드 호출 시 MemberMyPageResponse를 리턴한다.")
    public void test_my_page() {

        LoginMember loginMember = 로그인_맴버();
        Member findMember = 사용자();
        List<BookmarkFolderDto> findFolders = Arrays.asList(일반_폴더_DTO());

        when(reviewRepository.countReviewsByMemberId(loginMember.getId())).thenReturn(리뷰_개수);
        when(bookmarkFolderService.getBookmarkFolderDtos(loginMember.getId())).thenReturn(findFolders);
        when(memberRepository.getById(loginMember.getId())).thenReturn(findMember);

        MemberMyPageResponse response = memberService.myPage(loginMember);


        assertAll(() -> {
            assertThat(response.getNickname()).isEqualTo(findMember.getNickname());
            assertThat(response.getPicture()).isEqualTo(findMember.getPicture());
            assertThat(response.getReviewCount()).isEqualTo(리뷰_개수);
            assertThat(response.getFolderCount()).isEqualTo(findFolders.size());
            assertThat(response.getFolders().size()).isEqualTo(findFolders.size());
        });
    }


}