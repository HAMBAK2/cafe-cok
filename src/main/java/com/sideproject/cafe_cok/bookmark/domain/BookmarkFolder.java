package com.sideproject.cafe_cok.bookmark.domain;

import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.cafe_cok.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "bookmark_folders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFolder {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible;

    @Column(name = "is_default_folder", nullable = false)
    private Boolean isDefaultFolder;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @OneToMany(mappedBy = "bookmarkFolder", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    public BookmarkFolder(final String name,
                          final String color,
                          final Boolean isVisible,
                          final Boolean isDefaultFolder,
                          final Member member) {
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
        this.isDefaultFolder = isDefaultFolder;
        if(member != null) changeMember(member);
    }

    public void change(final BookmarkFolderUpdateRequest request) {
        this.name = request.getName();
        this.color = request.getColor();
        this.isVisible = request.getIsVisible();
    }

    public void changeVisible() {
        this.isVisible = !isVisible;
    }

    public void changeMember(final Member member) {
        this.member = member;
        member.getBookmarkFolders().add(this);
    }
}
