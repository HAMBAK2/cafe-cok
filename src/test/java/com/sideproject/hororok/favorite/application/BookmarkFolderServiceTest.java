package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.favorite.domain.BookmarkFolderRepository;
import com.sideproject.hororok.favorite.domain.BookmarkRepository;
import com.sideproject.hororok.favorite.dto.BookmarkFolderDto;
import com.sideproject.hororok.favorite.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.hororok.favorite.dto.response.BookmarkFoldersResponse;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.sideproject.hororok.common.fixtures.FavoriteFolderFixtures.*;
import static com.sideproject.hororok.common.fixtures.LoginMemberFixtures.*;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


class BookmarkFolderServiceTest {

    @Mock
    private BookmarkFolderRepository bookmarkFolderRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BookmarkFolderService bookmarkFolderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자가 소유한 폴더 정보를 조회")
    public void testBookmarkFolders() {

        //given
        LoginMember loginMember = 로그인_맴버();
        when(bookmarkFolderRepository
                .countByMemberId(loginMember.getId()))
                .thenReturn(폴더_개수);

        Member member = 사용자();
        BookmarkFolder folder1 = 폴더1(member);
        BookmarkFolder folder2 = 폴더2(member);
        when(bookmarkFolderRepository
                .findByMemberId(로그인_맴버_아이디))
                .thenReturn(Arrays.asList(folder1, folder2));

        //when
        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);

        //then
        assertThat(response.getFolderCount()).isEqualTo(폴더_개수);
        assertThat(response.getFolders().size()).isEqualTo(폴더_리스트_사이즈);
        assertThat(response.getFolders().get(폴더_리스트_인덱스1).getName()).isEqualTo(즐겨찾기_폴더_이름1);
        assertThat(response.getFolders().get(폴더_리스트_인덱스2).getName()).isEqualTo(즐겨찾기_폴더_이름2);
    }

    @Test
    @DisplayName("즐겨찾기 폴더 도메인을 DTO 리스트로 변경하는 기능")
    public void testMapToBookmarkFolderDtoList() {
        // Given
        Member member = 사용자();
        BookmarkFolder folder1 = 폴더1(member);
        BookmarkFolder folder2 = 폴더2(member);
        List<BookmarkFolder> bookmarkFolders = Arrays.asList(folder1, folder2);

        // When
        List<BookmarkFolderDto> bookmarkFolderDtos = bookmarkFolderService.mapToBookmarkFolderDtoList(bookmarkFolders);

        // Then
        assertThat(bookmarkFolderDtos.size()).isEqualTo(bookmarkFolders.size());
        for(int i = 0; i < bookmarkFolders.size(); i++) {
            assertThat(bookmarkFolderDtos.get(i).getName()).isEqualTo(bookmarkFolders.get(i).getName());
            assertThat(bookmarkFolderDtos.get(i).getColor()).isEqualTo(bookmarkFolders.get(i).getColor());
            assertThat(bookmarkFolderDtos.get(i).getFolderId()).isEqualTo(bookmarkFolders.get(i).getId());
        }
    }

    @Test
    @DisplayName("폴더를 저장하는 기능을 테스트한다.")
    public void testSave(){

        // Given
        LoginMember fakeLoginMember = 로그인_맴버();
        BookmarkFolderSaveRequest fakeRequest = 폴더_저장_요청();

        // When
        Member fakeMember = 사용자();
        System.out.println(fakeMember.getId());
        when(memberRepository.findById(fakeLoginMember.getId()))
                .thenReturn(Optional.of(fakeMember));

        BookmarkFolder fakeFolder = 폴더1(fakeMember);
        when(bookmarkFolderRepository.findById(fakeMember.getId()))
                .thenReturn(Optional.of(fakeFolder));

        BookmarkFoldersResponse response = bookmarkFolderService.save(fakeRequest, fakeLoginMember);

        //Then
        verify(bookmarkFolderRepository, times(1)).save(any(BookmarkFolder.class));
        assertThat(response).isNotNull();
    }
}