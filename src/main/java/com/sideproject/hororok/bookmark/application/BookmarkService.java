package com.sideproject.hororok.bookmark.application;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.cafe.exception.NoSuchCafeException;
import com.sideproject.hororok.bookmark.domain.Bookmark;
import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.bookmark.domain.BookmarkFolderRepository;
import com.sideproject.hororok.bookmark.domain.BookmarkRepository;
import com.sideproject.hororok.bookmark.dto.BookmarkDto;
import com.sideproject.hororok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.hororok.bookmark.dto.response.BookmarksResponse;
import com.sideproject.hororok.bookmark.exception.NoSuchFolderException;
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
    public void save(BookmarkSaveRequest request) {

        Cafe findCafe = cafeRepository.findById(request.getCafeId())
                .orElseThrow(() -> new NoSuchCafeException());

        BookmarkFolder findFolder = bookmarkFolderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new NoSuchFolderException());

        bookmarkRepository.save(new Bookmark(findCafe, findFolder));
    }

    @Transactional
    public void delete(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);

    }

}
