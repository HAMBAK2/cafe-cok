package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.exception.NoSuchFolderException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkFolderRepository extends JpaRepository<BookmarkFolder, Long>, BookmarkFolderRepositoryCustom {

    Long countByMemberId(Long memberId);
    List<BookmarkFolder> findByMemberId(Long memberId);

    default BookmarkFolder getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchFolderException::new);
    }

}
