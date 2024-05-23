package com.sideproject.cafe_cok.bookmark.dto.response;


import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class BookmarkFoldersResponse {

    private Long folderCount;
    private List<BookmarkFolderDto> folders;

    public static BookmarkFoldersResponse of(final Long folderCount, final List<BookmarkFolder> bookmarkFolders) {

        return BookmarkFoldersResponse.builder()
                .folderCount(folderCount)
                .folders(BookmarkFolderDto.fromList(bookmarkFolders))
                .build();
    }

}
