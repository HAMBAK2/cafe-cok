package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.favorite.domain.BookmarkFolderRepository;
import com.sideproject.hororok.favorite.domain.BookmarkRepository;
import com.sideproject.hororok.favorite.dto.BookmarkFolderDto;
import com.sideproject.hororok.favorite.dto.response.BookmarkFoldersResponse;
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
                                .of(folder, bookmarkRepository.countByFavoriteFolderId(folder.getId())))
                .collect(Collectors.toList());
    }




}
