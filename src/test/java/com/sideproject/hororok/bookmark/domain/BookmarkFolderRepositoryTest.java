package com.sideproject.hororok.bookmark.domain;

import com.sideproject.hororok.common.annotation.RepositoryTest;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.*;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.*;
import static org.assertj.core.api.Assertions.*;

class BookmarkFolderRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkFolderRepository bookmarkFolderRepository;
    
    private final Long FOLDER_COUNT = 1L;
    private final Integer FOLDER_SIZE = 1;

    @Test
    @DisplayName("맴버 아이디를 사용해서 즐겨찾기 폴더의 개수를 조회한다.")
    public void testCountByMemberId() {

       //given
        Member member = memberRepository.save(사용자());
        BookmarkFolder folder = bookmarkFolderRepository.save(폴더1(member));

        //when
        Long count = bookmarkFolderRepository.countByMemberId(member.getId());

        //then
        assertThat(count).isEqualTo(FOLDER_COUNT);
    }


    @Test
    @DisplayName("맴버 아이디를 사용해서 즐겨찾기 폴더의 리스트를 조회한다.")
    public void testFindByMemberId() {

        //given
        Member member = memberRepository.save(사용자());
        BookmarkFolder folder = bookmarkFolderRepository.save(폴더1(member));

        //when
        List<BookmarkFolder> folders = bookmarkFolderRepository.findByMemberId(member.getId());
        BookmarkFolder findFolder = folders.get(0);

        //then
        assertThat(folders.size()).isEqualTo(FOLDER_SIZE);
        assertThat(findFolder.getName()).isEqualTo(즐겨찾기_폴더_이름1);
        assertThat(findFolder.getColor()).isEqualTo(즐겨찾기_폴더_색상1);
        assertThat(findFolder.getIsVisible()).isEqualTo(즐겨찾기_폴더_노출여부1);
    }


}