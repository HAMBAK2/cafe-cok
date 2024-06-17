package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.exception.NoSuchBookmarkException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom{

    Long deleteByBookmarkFolderId(final Long folderId);

    @Query("SELECT b " +
            "FROM Bookmark b JOIN BookmarkFolder bf ON b.bookmarkFolder.id = bf.id " +
            "WHERE b.cafe.id = :cafeId " +
                "AND bf.member.id = :memberId")
    List<Bookmark> findByCafeIdAndMemberId(final Long cafeId, final Long memberId);

    default Bookmark getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchBookmarkException::new);
    }
}
