package com.sideproject.cafe_cok.bookmark.domain.repository;


import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.exception.NoSuchBookmarkException;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static com.sideproject.cafe_cok.constant.TestConstants.CAFE_PHONE_NUMBER;
import static com.sideproject.cafe_cok.constant.TestConstants.CAFE_ROAD_ADDRESS;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkFolderRepository bookmarkFolderRepository;

    @Test
    @DisplayName("북마크 id를 기반으로 북마크를 조회한다.")
    void get_by_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        BookmarkFolder bookmarkFolder = new BookmarkFolder(BOOKMARK_FOLDER_NAME_1, BOOKMARK_FOLDER_COLOR_1,
                BOOKMARK_IS_VISIBLE, BOOKMARK_IS_DEFAULT_FOLDER, savedMember);
        BookmarkFolder savedBookmarkFolder = bookmarkFolderRepository.save(bookmarkFolder);
        Bookmark bookmark = new Bookmark(savedCafe, savedBookmarkFolder);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        //when
        Bookmark findBookmark = bookmarkRepository.getById(savedBookmark.getId());

        //then
        assertThat(findBookmark).isNotNull();
        assertThat(findBookmark.getId()).isEqualTo(savedBookmark.getId());
        assertThat(findBookmark.getCafe()).isEqualTo(savedCafe);
        assertThat(findBookmark.getBookmarkFolder()).isEqualTo(savedBookmarkFolder);
    }

    @Test
    @DisplayName("존재하지 않는 북마크 Id로 조회 시 에러를 발생시킨다.")
    void get_by_non_existent_id() {

        //when & then
        assertThatExceptionOfType(NoSuchBookmarkException.class)
                .isThrownBy(() -> bookmarkRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 북마크가 존재하지 않습니다.");
    }
}