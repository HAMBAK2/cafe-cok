package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.favorite.domain.Bookmark;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.favorite.domain.BookmarkFolderRepository;
import com.sideproject.hororok.favorite.domain.BookmarkRepository;
import com.sideproject.hororok.favorite.dto.BookmarkDto;
import com.sideproject.hororok.favorite.dto.response.BookmarksResponse;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.joda.time.field.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sideproject.hororok.common.fixtures.BookmarkFixtures.북마크;
import static com.sideproject.hororok.common.fixtures.BookmarkFixtures.북마크_DTO;
import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.*;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페2;
import static com.sideproject.hororok.common.fixtures.LoginMemberFixtures.로그인_맴버;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookmarkServiceTest {

    @Mock
    private BookmarkFolderRepository bookmarkFolderRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private CafeRepository cafeRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("북마크 폴더 선택 시 북마크 리스트 Response를 리턴한다.")
    public void test_bookmarks() {

        long folderId = 1L;

        BookmarkFolder mockFolder = 폴더1(사용자());
        setId(mockFolder, folderId);
        List<Bookmark> mockBookmarks = new ArrayList<>();
        mockBookmarks.add(북마크(카페(), mockFolder));
        mockBookmarks.add(북마크(카페2(), mockFolder));

        when(bookmarkFolderRepository.findById(folderId)).thenReturn(Optional.of(mockFolder));
        when(bookmarkRepository.findByBookmarkFolderId(folderId)).thenReturn(mockBookmarks);

        BookmarksResponse response = bookmarkService.bookmarks(folderId);

        assertAll(() -> {
            assertThat(response.getFolderId()).isEqualTo(folderId);
            assertThat(response.getFolderName()).isEqualTo(mockFolder.getName());
            assertThat(response.getColor()).isEqualTo(mockFolder.getColor());
            assertThat(response.getBookmarks().size()).isEqualTo(mockBookmarks.size());
        });

    }

}