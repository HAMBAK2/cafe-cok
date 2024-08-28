package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.exception.NoSuchBookmarkException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom{

    default Bookmark getById(final Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new NoSuchBookmarkException("[ID : " + id + "] 에 해당하는 북마크가 존재하지 않습니다."));
    }
}
