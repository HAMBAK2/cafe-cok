package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageTagSaveResponse {

    private Integer folderCount;
    private List<BookmarkFolderDto> folders;

    public static MyPageTagSaveResponse of(final Integer folderCount, final List<BookmarkFolderDto> folders) {

        return MyPageTagSaveResponse.builder()
                .folderCount(folderCount)
                .folders(folders)
                .build();
    }
}
