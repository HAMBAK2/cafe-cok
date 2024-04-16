package com.sideproject.hororok.bookmark.domain;


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
}