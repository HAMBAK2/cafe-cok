package com.sideproject.hororok.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteFolderRepository extends JpaRepository<FavoriteFolder, Long> {

    Long countByMemberId(Long memberId);
    List<FavoriteFolder> findByMemberId(Long memberId);

}
