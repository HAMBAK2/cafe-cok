package com.sideproject.hororok.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkFolderRepository extends JpaRepository<BookmarkFolder, Long> {

    Long countByMemberId(Long memberId);
    List<BookmarkFolder> findByMemberId(Long memberId);

}
