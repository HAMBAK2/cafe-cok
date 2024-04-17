package com.sideproject.hororok.favorite.dto.response;


import com.sideproject.hororok.favorite.domain.FavoriteFolder;
import com.sideproject.hororok.favorite.dto.FavoriteFolderDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MyPlaceResponse {

    private Long folderCount;
    private List<FavoriteFolderDto> folders;

    private MyPlaceResponse() {
    }

    public MyPlaceResponse(final Long folderCount) {
        this.folderCount = folderCount;
        this.folders = new ArrayList<>();
    }

    public MyPlaceResponse(final Long folderCount,
                           final List<FavoriteFolderDto> folders) {
        this.folderCount = folderCount;
        this.folders =folders;
    }

}
