package com.sideproject.cafe_cok.bookmark.application;

import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarksResponse;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolderRepository;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkRepository;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final CafeRepository cafeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;

    public BookmarksResponse bookmarks(Long folderId){

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);

        List<Bookmark> findBookmarks
                = bookmarkRepository.findByBookmarkFolderId(folderId);

        if(findBookmarks.isEmpty()) {
            return new BookmarksResponse(
                    findFolder.getId(), findFolder.getName(),
                    findFolder.getColor());
        }

        List<BookmarkDto> convertBookmarks
                = findBookmarks.stream()
                .map(bookmark -> BookmarkDto.from(bookmark))
                .collect(Collectors.toList());

        return new BookmarksResponse(
                findFolder.getId(), findFolder.getName(),
                findFolder.getColor(), convertBookmarks);
    }

    @Transactional
    public BookmarkIdResponse save(BookmarkSaveRequest request) {

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        BookmarkFolder findFolder = bookmarkFolderRepository.getById(request.getFolderId());

        Bookmark savedBookmark = bookmarkRepository.save(new Bookmark(findCafe, findFolder));
        return BookmarkIdResponse.of(savedBookmark.getId());
    }

    @Transactional
    public BookmarkIdResponse delete(final Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);

        return BookmarkIdResponse.of(bookmarkId);

    }

}
