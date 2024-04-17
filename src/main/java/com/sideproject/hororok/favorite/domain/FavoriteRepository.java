package com.sideproject.hororok.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Long countByFavoriteFolderId(Long favoriteFolderId);
}
