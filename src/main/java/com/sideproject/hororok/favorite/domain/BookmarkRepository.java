package com.sideproject.hororok.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Long countByBookmarkFolderId(Long favoriteFolderId);
}
