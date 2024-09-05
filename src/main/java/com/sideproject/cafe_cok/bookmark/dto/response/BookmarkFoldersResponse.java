package com.sideproject.cafe_cok.bookmark.dto.response;


import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderCountDto;
import com.sideproject.cafe_cok.cafe.dto.response.CafeTopResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFoldersResponse extends RepresentationModel<BookmarkFoldersResponse> {

    private Integer folderCount;
    private List<BookmarkFolderCountDto> folders;

    public BookmarkFoldersResponse(final List<BookmarkFolderCountDto> folders) {
        this.folderCount = folders.size();
        this.folders = folders;
    }
}
