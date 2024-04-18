package com.sideproject.hororok.favorite.domain;

import com.sideproject.hororok.favorite.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.hororok.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;


@Getter
@Entity
@Table(name = "bookmark_folders")
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    protected BookmarkFolder() {
    }

    public BookmarkFolder(final String name, final String color,
                          final Boolean isVisible, final Member member) {
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
        this.member = member;
    }

    public void change(BookmarkFolderUpdateRequest request) {
        this.name = request.getName();
        this.color = request.getColor();
        this.isVisible = request.getIsVisible();
    }

    public void changeVisible() {
        this.isVisible = !isVisible;
    }
}
