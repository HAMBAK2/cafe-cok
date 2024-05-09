package com.sideproject.cafe_cok.bookmark.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Long countByBookmarkFolderId(Long bookmarkFolderId);

    List<Bookmark> findByBookmarkFolderId(Long bookmarkFolderId);

    Long deleteByBookmarkFolderId(Long folderId);

    @Query("SELECT b " +
            "FROM Bookmark b JOIN BookmarkFolder bf ON b.bookmarkFolder.id = bf.id " +
            "WHERE b.cafe.id = :cafeId " +
                "AND bf.member.id = :memberId")
    List<Bookmark> findByCafeIdAndMemberId(final Long cafeId, final Long memberId);

}
