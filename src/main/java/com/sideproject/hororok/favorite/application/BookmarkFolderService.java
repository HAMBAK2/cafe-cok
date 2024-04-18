package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.favorite.domain.BookmarkFolderRepository;
import com.sideproject.hororok.favorite.domain.BookmarkRepository;
import com.sideproject.hororok.favorite.dto.BookmarkFolderDto;
import com.sideproject.hororok.favorite.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.hororok.favorite.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.hororok.favorite.dto.response.BookmarkFoldersResponse;
import com.sideproject.hororok.favorite.exception.DefaultFolderDeletionNotAllowedException;
import com.sideproject.hororok.favorite.exception.NoSuchFolderException;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import com.sideproject.hororok.member.exception.NoSuchMemberException;
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

        List<BookmarkFolderDto> folders =
                mapToBookmarkFolderDtoList(bookmarkFolderRepository.findByMemberId(memberId));

        return new BookmarkFoldersResponse(count, folders);
    }

    public List<BookmarkFolderDto> mapToBookmarkFolderDtoList(List<BookmarkFolder> folders) {

        return folders.stream()
                .map(folder ->
                        BookmarkFolderDto
                                .of(folder, bookmarkRepository.countByBookmarkFolderId(folder.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookmarkFoldersResponse save(BookmarkFolderSaveRequest request, LoginMember loginMember){

        Member findMember = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NoSuchMemberException());
        BookmarkFolder bookmarkFolder = request.toBookmarkFolder(findMember);
        bookmarkFolderRepository.save(bookmarkFolder);
        return bookmarkFolders(loginMember);
    }

    @Transactional
    public BookmarkFoldersResponse update(BookmarkFolderUpdateRequest request, LoginMember loginMember){

        Member findMember = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NoSuchMemberException());

        BookmarkFolder findFolder = bookmarkFolderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new NoSuchFolderException());

        findFolder.change(request);
        bookmarkFolderRepository.save(findFolder);
        return bookmarkFolders(loginMember);
    }

    @Transactional
    public void updateFolderVisible(Long folderId) {

        BookmarkFolder findFolder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new NoSuchFolderException());

        findFolder.changeVisible();
        bookmarkFolderRepository.save(findFolder);
    }

    @Transactional
    public BookmarkFoldersResponse delete(Long folderId, LoginMember loginMember) {

        if(validateDefaultFolder(folderId)) {
            throw new DefaultFolderDeletionNotAllowedException();
        }

        bookmarkFolderRepository.deleteById(folderId);
        return bookmarkFolders(loginMember);
    }

    private boolean validateDefaultFolder(Long folderId) {
        BookmarkFolder findFolder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new NoSuchFolderException());

        return findFolder.getIsDefaultFolder();
    }

}
