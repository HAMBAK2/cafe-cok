package com.sideproject.hororok.bookmark.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.bookmark.domain.BookmarkFolderRepository;
import com.sideproject.hororok.bookmark.domain.BookmarkRepository;
import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.hororok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.hororok.bookmark.dto.response.BookmarkFolderDeleteResponse;
import com.sideproject.hororok.bookmark.dto.response.BookmarkFoldersResponse;
import com.sideproject.hororok.bookmark.exception.DefaultFolderDeletionNotAllowedException;
import com.sideproject.hororok.bookmark.exception.DefaultFolderUpdateNotAllowedException;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFolderService {

    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    public BookmarkFoldersResponse bookmarkFolders(LoginMember loginMember) {

        Long memberId = loginMember.getId();
        Long count = bookmarkFolderRepository.countByMemberId(memberId);
        List<BookmarkFolderDto> folders = getBookmarkFolderDtos(memberId);

        return new BookmarkFoldersResponse(count, folders);
    }

    @Transactional
    public void save(BookmarkFolderSaveRequest request, LoginMember loginMember){

        Member findMember = memberRepository.getById(loginMember.getId());
        BookmarkFolder bookmarkFolder = request.toBookmarkFolder(findMember);
        bookmarkFolderRepository.save(bookmarkFolder);
    }

    @Transactional
    public void update(BookmarkFolderUpdateRequest request){

        BookmarkFolder findFolder = updateFolderIfPermissible(request);
        findFolder.change(request);
        bookmarkFolderRepository.save(findFolder);
    }

    @Transactional
    public void updateFolderVisible(Long folderId) {

        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);
        findFolder.changeVisible();
        bookmarkFolderRepository.save(findFolder);
    }

    @Transactional
    public BookmarkFolderDeleteResponse delete(Long folderId) {

        deleteFolderIfPermissible(folderId);
        bookmarkRepository.deleteByBookmarkFolderId(folderId);
        bookmarkFolderRepository.deleteById(folderId);

        return new BookmarkFolderDeleteResponse(folderId);
    }

    public List<BookmarkFolderDto> getBookmarkFolderDtos(Long memberId) {
        List<BookmarkFolder> folders = bookmarkFolderRepository.findByMemberId(memberId);

        return folders.stream()
                .map(folder ->
                        new BookmarkFolderDto(folder, bookmarkRepository.countByBookmarkFolderId(folder.getId())))
                .collect(Collectors.toList());
    }

    public void deleteFolderIfPermissible(Long folderId) {
        BookmarkFolder findFolder = bookmarkFolderRepository.getById(folderId);
        if(findFolder.getIsDefaultFolder())
            throw new DefaultFolderDeletionNotAllowedException();
    }

    public BookmarkFolder updateFolderIfPermissible(BookmarkFolderUpdateRequest request) {
        BookmarkFolder findFolder = bookmarkFolderRepository.getById(request.getFolderId());

        if(!findFolder.getIsDefaultFolder()) return findFolder;
        if(findFolder.getName().equals(request.getName())
                && findFolder.getColor().equals(request.getColor())) return findFolder;

        throw new DefaultFolderUpdateNotAllowedException();
    }

}
