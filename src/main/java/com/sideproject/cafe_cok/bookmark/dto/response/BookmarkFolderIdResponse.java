package com.sideproject.cafe_cok.bookmark.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class BookmarkFolderIdResponse extends RepresentationModel<BookmarkFolderIdResponse> {

    private Long folderId;

    public BookmarkFolderIdResponse(final Long folderId) {
        this.folderId = folderId;
    }
}
