package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.favorite.domain.Favorite;
import com.sideproject.hororok.favorite.domain.FavoriteFolder;

public class FavoriteFixtures {

    public static final Long 폴더_즐겨찾기_개수 = 2L;


    public static Favorite 즐겨찾기(final Cafe cafe, final FavoriteFolder favoriteFolder) {
        return new Favorite(cafe, favoriteFolder);
    }
}
