package com.sideproject.hororok.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Long countByBookmarkFolderId(Long bookmarkFolderId);

    List<Bookmark> findByBookmarkFolderId(Long bookmarkFolderId);
}
