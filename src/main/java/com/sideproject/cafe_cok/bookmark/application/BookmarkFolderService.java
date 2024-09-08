package com.sideproject.cafe_cok.bookmark.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkCafeDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDetailDto;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderIdResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFoldersResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarksResponse;
import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderDeletionNotAllowedException;
import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderUpdateNotAllowedException;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkFolderRepository;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFolderService {

    private final MemberRepository memberRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;

    public BookmarksResponse find(final Long folderId){

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);
        List<BookmarkCafeDto> findBookmarkCafeDtoList
                = findFolder.getBookmarks().stream()
                .map(bookmark -> new BookmarkCafeDto(bookmark.getId(), bookmark.getCafe()))
                .collect(Collectors.toList());
        return new BookmarksResponse(findFolder, findBookmarkCafeDtoList);
    }

    public BookmarkFoldersResponse bookmarkFolders(final LoginMember loginMember) {

        List<BookmarkFolderDetailDto> findBookmarkFolderDetailDtoList =
                bookmarkFolderRepository.getBookmarkFolderDetails(loginMember.getId());
        return new BookmarkFoldersResponse(findBookmarkFolderDetailDtoList);
    }

    @Transactional
    public BookmarkFolderIdResponse save(final BookmarkFolderSaveRequest request,
                                         final LoginMember loginMember){

        Member findMember = memberRepository.getById(loginMember.getId());
        BookmarkFolder bookmarkFolder = request.toEntity(findMember);
        BookmarkFolder savedBookmarkFolder = bookmarkFolderRepository.save(bookmarkFolder);
        return new BookmarkFolderIdResponse(savedBookmarkFolder.getId());
    }

    @Transactional
    public BookmarkFolderIdResponse update(final BookmarkFolderUpdateRequest request){
        bookmarkFolderRepository.update(request);
        return new BookmarkFolderIdResponse(request.getFolderId());
    }

    @Transactional
    public BookmarkFolderIdResponse updateFolderVisible(final Long folderId) {

        bookmarkFolderRepository.toggleFolderVisibility(folderId);
        return new BookmarkFolderIdResponse(folderId);
    }

    @Transactional
    public BookmarkFolderIdResponse delete(final Long folderId) {

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);
        if(findFolder.getIsDefaultFolder()) throw new DefaultFolderDeletionNotAllowedException();
        bookmarkFolderRepository.deleteById(folderId);
        return new BookmarkFolderIdResponse(folderId);
    }
}
