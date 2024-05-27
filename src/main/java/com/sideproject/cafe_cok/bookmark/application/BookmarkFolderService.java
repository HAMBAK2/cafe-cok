package com.sideproject.cafe_cok.bookmark.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkRepository;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderDeleteResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFoldersResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarksResponse;
import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderDeletionNotAllowedException;
import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderUpdateNotAllowedException;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolderRepository;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFolderService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;

    public BookmarksResponse bookmarks(final Long folderId){

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);
        List<Bookmark> findBookmarks = bookmarkRepository.findByBookmarkFolderId(folderId);
        if(findBookmarks.isEmpty()) return BookmarksResponse.from(findFolder);
        return BookmarksResponse.from(findFolder, findBookmarks);
    }

    public BookmarkFoldersResponse bookmarkFolders(final LoginMember loginMember) {

        Long memberId = loginMember.getId();
        Long folderCount = bookmarkFolderRepository.countByMemberId(memberId);
        List<BookmarkFolder> findFolders = bookmarkFolderRepository.findByMemberId(memberId);
        return BookmarkFoldersResponse.of(folderCount, findFolders);
    }

    @Transactional
    public void save(final BookmarkFolderSaveRequest request, final LoginMember loginMember){

        Member findMember = memberRepository.getById(loginMember.getId());
        BookmarkFolder bookmarkFolder = request.toBookmarkFolder(findMember);
        bookmarkFolderRepository.save(bookmarkFolder);
    }

    @Transactional
    public void update(final BookmarkFolderUpdateRequest request){

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(request.getFolderId());
        if(findFolder.getIsDefaultFolder()) throw new DefaultFolderUpdateNotAllowedException();
        findFolder.change(request);
        bookmarkFolderRepository.save(findFolder);
    }

    @Transactional
    public void updateFolderVisible(final Long folderId) {

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);
        findFolder.changeVisible();
        bookmarkFolderRepository.save(findFolder);
    }

    @Transactional
    public BookmarkFolderDeleteResponse delete(final Long folderId) {

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);
        if(findFolder.getIsDefaultFolder()) throw new DefaultFolderDeletionNotAllowedException();
        bookmarkFolderRepository.deleteById(folderId);
        return new BookmarkFolderDeleteResponse(folderId);
    }
}
