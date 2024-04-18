package com.sideproject.hororok.favorite.dto.response;


import com.sideproject.hororok.favorite.dto.BookmarkFolderDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookmarkFoldersResponse {

    private Long folderCount;
    private List<BookmarkFolderDto> folders;

    private BookmarkFoldersResponse() {
    }

    public BookmarkFoldersResponse(final Long folderCount) {
        this.folderCount = folderCount;
        this.folders = new ArrayList<>();
    }

    public BookmarkFoldersResponse(final Long folderCount,
                                   final List<BookmarkFolderDto> folders) {
        this.folderCount = folderCount;
        this.folders =folders;
    }

}
