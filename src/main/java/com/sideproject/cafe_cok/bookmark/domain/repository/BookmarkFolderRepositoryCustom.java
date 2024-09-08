package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDetailDto;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;

import java.util.List;

public interface BookmarkFolderRepositoryCustom {

    void update(final BookmarkFolderUpdateRequest request);

    void toggleFolderVisibility(final Long folderId);

    List<BookmarkFolderDetailDto> getBookmarkFolderDetails(final Long memberId);




}
