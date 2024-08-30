package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.exception.NoSuchFolderException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkFolderRepository extends JpaRepository<BookmarkFolder, Long>, BookmarkFolderRepositoryCustom {

    List<BookmarkFolder> findByMemberId(final Long memberId);

    default BookmarkFolder getById(final Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new NoSuchFolderException("[ID : " + id + "] 에 해당하는 북마크 폴더가 존재하지 않습니다."));
    }
}
