package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.favorite.domain.FavoriteFolder;
import com.sideproject.hororok.favorite.dto.FavoriteFolderDto;
import com.sideproject.hororok.favorite.dto.response.MyPlaceResponse;
import com.sideproject.hororok.member.domain.Member;

import java.util.List;


public class FavoriteFolderFixtures {

    public static final Long 폴더_있을때_개수 = 2L;
    public static final Integer 폴더_있을때_리스트_사이즈 = 2;
    public static final Integer 폴더_있을때_리스트_인덱스1 = 0;
    public static final Integer 폴더_있을때_리스트_인덱스2 = 1;
    public static final Long 폴더_있을때_ID_1 = 1L;
    public static final Long 폴더_있을때_ID_2 = 2L;

    /* 폴더1 */
    public static final String 즐겨찾기_폴더_이름1 = "즐겨찾기_폴더_이름1";
    public static final String 즐겨찾기_폴더_색상1 = "즐겨찾기_폴더_색상1";
    public static final Boolean 즐겨찾기_폴더_노출여부1 = true;

    /* 폴더2 */
    public static final String 즐겨찾기_폴더_이름2 = "즐겨찾기_폴더_이름2";
    public static final String 즐겨찾기_폴더_색상2 = "즐겨찾기_폴더_색상2";
    public static final Boolean 즐겨찾기_폴더_노출여부2 = true;


    public static FavoriteFolder 폴더1(final Member member) {

        return new FavoriteFolder(즐겨찾기_폴더_이름1, 즐겨찾기_폴더_색상1, 즐겨찾기_폴더_노출여부1, member);
    }

    public static FavoriteFolder 폴더2(final Member member) {

        return new FavoriteFolder(즐겨찾기_폴더_이름2, 즐겨찾기_폴더_색상2, 즐겨찾기_폴더_노출여부2, member);
    }

    public static FavoriteFolderDto 폴더_Dto(
            final Long folderId, final String name,
            final String color, final Boolean isVisible){
        return FavoriteFolderDto.of(
                folderId, name, color, isVisible);
    }

    public static MyPlaceResponse 마이_플레이스_응답(
            final Long count,
            List<FavoriteFolderDto> folders) {

        return new MyPlaceResponse(count, folders);
    }
}
