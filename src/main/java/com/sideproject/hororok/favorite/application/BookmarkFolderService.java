package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.favorite.domain.BookmarkFolderRepository;
import com.sideproject.hororok.favorite.domain.BookmarkRepository;
import com.sideproject.hororok.favorite.dto.BookmarkFolderDto;
import com.sideproject.hororok.favorite.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.hororok.favorite.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.hororok.favorite.dto.response.BookmarkFoldersResponse;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
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
                .orElseThrow(() -> new EntityNotFoundException("맴버가 존재하지 않습니다."));
        BookmarkFolder bookmarkFolder = request.toBookmarkFolder(findMember);
        bookmarkFolderRepository.save(bookmarkFolder);
        return bookmarkFolders(loginMember);
    }

    @Transactional
    public BookmarkFoldersResponse update(BookmarkFolderUpdateRequest request, LoginMember loginMember){

        Member findMember = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new EntityNotFoundException("맴버가 존재하지 않습니다."));

        BookmarkFolder findFolder = bookmarkFolderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new EntityNotFoundException("폴더가 존재하지 않습니다."));

        findFolder.change(request);
        bookmarkFolderRepository.save(findFolder);
        return bookmarkFolders(loginMember);
    }

    @Transactional
    public void updateFolderVisible(Long folderId) {

        BookmarkFolder findFolder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("폴더가 존재하지 않습니다."));

        findFolder.changeVisible();
        bookmarkFolderRepository.save(findFolder);
    }

}
