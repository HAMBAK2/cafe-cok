package com.sideproject.hororok.favorite.dto.response;


import com.sideproject.hororok.favorite.domain.FavoriteFolder;
import com.sideproject.hororok.favorite.dto.FavoriteFolderDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MyPlaceResponse {

    private Long count;
    private List<FavoriteFolderDto> folders;

    private MyPlaceResponse() {
    }

    public MyPlaceResponse(final Long count) {
        this.count = count;
        this.folders = new ArrayList<>();
    }

    public MyPlaceResponse(final Long count,
                           final List<FavoriteFolderDto> folders) {
        this.count = count;
        this.folders =folders;
    }

}
