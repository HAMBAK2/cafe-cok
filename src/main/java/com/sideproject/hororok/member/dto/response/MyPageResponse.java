package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.member.domain.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class MyPageResponse {

    private String nickname;
    private String picture;
    private Long reviewCount;
    private Integer folderCount;
    private List<BookmarkFolderDto> folders;

    private MyPageResponse() {
    }

    public MyPageResponse(final Member member,
                          final Long reviewCount,
                          final List<BookmarkFolderDto> folders) {
        this.nickname = member.getNickname();
        this.picture = member.getPicture();
        this.reviewCount = reviewCount;
        this.folderCount = folders.size();
        this.folders = folders;
    }
}
