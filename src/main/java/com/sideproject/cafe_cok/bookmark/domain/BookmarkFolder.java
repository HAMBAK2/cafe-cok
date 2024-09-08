package com.sideproject.cafe_cok.bookmark.domain;

import com.sideproject.cafe_cok.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "bookmark_folders")
@NoArgsConstructor
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

    @Builder
    public BookmarkFolder(final Long id,
                          final String name,
                          final String color,
                          final Boolean isVisible,
                          final Boolean isDefaultFolder,
                          final Member member) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
        this.isDefaultFolder = isDefaultFolder;
        if(member != null) changeMember(member);
    }

    public void changeMember(final Member member) {
        this.member = member;
        member.getBookmarkFolders().add(this);
    }
}
