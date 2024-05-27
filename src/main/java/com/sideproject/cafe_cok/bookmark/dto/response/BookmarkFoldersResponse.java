package com.sideproject.cafe_cok.bookmark.dto.response;


import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderCountDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFoldersResponse {

    private Integer folderCount;
    private List<BookmarkFolderCountDto> folders;

    public BookmarkFoldersResponse(final List<BookmarkFolderCountDto> folders) {
        this.folderCount = folders.size();
        this.folders = folders;
    }
}
