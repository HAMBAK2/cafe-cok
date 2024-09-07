package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDetailDto;
import com.sideproject.cafe_cok.bookmark.exception.NoSuchFolderException;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class BookmarkFolderRepositoryTest {

    @Autowired
    private BookmarkFolderRepository bookmarkFolderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;


    @Test
    @DisplayName("북마크 폴더 Id를 기반으로 북마크 폴더를 조회한다.")
    void get_by_id() {

        //given
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        BookmarkFolder bookmarkFolder = new BookmarkFolder(BOOKMARK_FOLDER_NAME_1, BOOKMARK_FOLDER_COLOR_1,
                BOOKMARK_IS_VISIBLE, BOOKMARK_IS_DEFAULT_FOLDER, savedMember);
        BookmarkFolder savedBookmarkFolder = bookmarkFolderRepository.save(bookmarkFolder);

        //when
        BookmarkFolder findBookmarkFolder = bookmarkFolderRepository.getById(savedBookmarkFolder.getId());

        //then
        assertThat(findBookmarkFolder).isNotNull();
        assertThat(findBookmarkFolder.getId()).isEqualTo(savedBookmarkFolder.getId());
        assertThat(findBookmarkFolder.getName()).isEqualTo(savedBookmarkFolder.getName());
        assertThat(findBookmarkFolder.getColor()).isEqualTo(savedBookmarkFolder.getColor());
        assertThat(findBookmarkFolder.getIsVisible()).isEqualTo(savedBookmarkFolder.getIsVisible());
        assertThat(findBookmarkFolder.getIsDefaultFolder()).isEqualTo(savedBookmarkFolder.getIsDefaultFolder());
        assertThat(findBookmarkFolder.getMember()).isEqualTo(savedBookmarkFolder.getMember());
    }

    @Test
    @DisplayName("존재하지 않는 북마크 폴더 id로 조회 시 에러를 발생시킨다.")
    void get_by_non_existent_id() {

        //when & then
        assertThatExceptionOfType(NoSuchFolderException.class)
                .isThrownBy(() -> bookmarkFolderRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 북마크 폴더가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("memberId를 기반으로 북마크 폴더의 목록을 조회한다.")
    void find_bookmark_folder_list_by_member_id() {

        //given
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        BookmarkFolder bookmarkFolder1 = new BookmarkFolder(BOOKMARK_FOLDER_NAME_1, BOOKMARK_FOLDER_COLOR_1,
                BOOKMARK_IS_VISIBLE, BOOKMARK_IS_DEFAULT_FOLDER, savedMember);
        BookmarkFolder bookmarkFolder2 = new BookmarkFolder(BOOKMARK_FOLDER_NAME_2, BOOKMARK_FOLDER_COLOR_2,
                BOOKMARK_IS_VISIBLE, BOOKMARK_IS_DEFAULT_FOLDER, savedMember);
        BookmarkFolder savedBookmarkFolder1 = bookmarkFolderRepository.save(bookmarkFolder1);
        BookmarkFolder savedBookmarkFolder2 = bookmarkFolderRepository.save(bookmarkFolder2);

        //when
        List<BookmarkFolder> findBookmarkFolders = bookmarkFolderRepository.findByMemberId(savedMember.getId());

        //then
        assertThat(findBookmarkFolders).hasSize(2);
        assertThat(findBookmarkFolders).extracting("name").containsExactlyInAnyOrder(BOOKMARK_FOLDER_NAME_1, BOOKMARK_FOLDER_NAME_2);
        assertThat(findBookmarkFolders).extracting("color").containsExactlyInAnyOrder(BOOKMARK_FOLDER_COLOR_1, BOOKMARK_FOLDER_COLOR_2);
    }

    @Test
    @DisplayName("존재하지 않는 memberId로 북마크 폴더 목록을 조회할 시 빈 리스트를 반환한다.")
    void find_bookmark_folder_list_by_non_existent_member_id() {

        //when
        List<BookmarkFolder> findBookmarkFolders = bookmarkFolderRepository.findByMemberId(NON_EXISTENT_ID);

        //then
        assertThat(findBookmarkFolders).isEmpty();
    }

    @Test
    @DisplayName("memberId를 기반으로 BookmarkFolderCountDto의 리스트를 조회한다.")
    void find_bookmark_folder_count_dto_list_by_member_id() {

        //given
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        BookmarkFolder bookmarkFolder1 = new BookmarkFolder(BOOKMARK_FOLDER_NAME_1, BOOKMARK_FOLDER_COLOR_1,
                BOOKMARK_IS_VISIBLE, BOOKMARK_IS_DEFAULT_FOLDER, savedMember);
        BookmarkFolder bookmarkFolder2 = new BookmarkFolder(BOOKMARK_FOLDER_NAME_2, BOOKMARK_FOLDER_COLOR_2,
                BOOKMARK_IS_VISIBLE, BOOKMARK_IS_DEFAULT_FOLDER, savedMember);
        BookmarkFolder savedBookmarkFolder1 = bookmarkFolderRepository.save(bookmarkFolder1);
        BookmarkFolder savedBookmarkFolder2 = bookmarkFolderRepository.save(bookmarkFolder2);
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Bookmark bookmark = new Bookmark(savedCafe, savedBookmarkFolder1);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        //when
        List<BookmarkFolderDetailDto> findBookmarkFolderDetailDtoList
                = bookmarkFolderRepository.getBookmarkFolderDetails(savedMember.getId());

        //then
        assertThat(findBookmarkFolderDetailDtoList).hasSize(2);
        assertThat(findBookmarkFolderDetailDtoList)
                .extracting("bookmarkCount")
                .containsExactlyInAnyOrder(savedBookmarkFolder1.getBookmarks().size(), savedBookmarkFolder2.getBookmarks().size());
    }

}