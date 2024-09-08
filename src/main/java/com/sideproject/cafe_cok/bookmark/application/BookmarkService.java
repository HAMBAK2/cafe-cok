package com.sideproject.cafe_cok.bookmark.application;

import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkFolderRepository;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderAndBookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkIdResponse;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final CafeRepository cafeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;

    @Transactional
    public BookmarkFolderAndBookmarkIdResponse save(BookmarkSaveRequest request) {

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        BookmarkFolder findFolder = bookmarkFolderRepository.getById(request.getFolderId());
        Bookmark savedBookmark = bookmarkRepository.save(Bookmark.builder()
                .cafe(findCafe)
                .bookmarkFolder(findFolder)
                .build());
        return new BookmarkFolderAndBookmarkIdResponse(
                savedBookmark.getId(), savedBookmark.getBookmarkFolder().getId());
    }

    @Transactional
    public BookmarkFolderAndBookmarkIdResponse delete(final Long bookmarkId) {
        Bookmark findBookmark = bookmarkRepository.getById(bookmarkId);
        bookmarkRepository.deleteById(bookmarkId);
        return new BookmarkFolderAndBookmarkIdResponse(
                findBookmark.getId(), findBookmark.getBookmarkFolder().getId());
    }
}
