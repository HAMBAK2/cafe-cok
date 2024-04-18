package com.sideproject.hororok.favorite.domain;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.common.annotation.RepositoryTest;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.hororok.common.fixtures.FavoriteFixtures.즐겨찾기;
import static com.sideproject.hororok.common.fixtures.FavoriteFolderFixtures.폴더1;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;
import static org.assertj.core.api.Assertions.*;

public class BookmarkRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BookmarkFolderRepository bookmarkFolderRepository;

    @Test
    @DisplayName("즐겨찾기 폴더의 ID와 연결된 즐겨찾기의 개수를 조회한다.")
    public void testCountByFavoriteFolderId() {

        //given
        Cafe savedCafe = cafeRepository.save(카페());
        Member savedMember = memberRepository.save(사용자());
        BookmarkFolder savedFolder = bookmarkFolderRepository.save(폴더1(savedMember));
        Bookmark savedBookmark = bookmarkRepository.save(즐겨찾기(savedCafe, savedFolder));

        //when
        Long count = bookmarkRepository.countByBookmarkFolderId(savedFolder.getId());

        //then
        assertThat(count).isEqualTo(1L);
    }
}
