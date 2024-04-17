package com.sideproject.hororok.favorite.domain;

import com.sideproject.hororok.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;


@Getter
@Entity
@Table(name = "favorite_folders")
public class FavoriteFolder {

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

    protected FavoriteFolder() {
    }

    public FavoriteFolder(final String name, final String color,
                          final Boolean isVisible, final Member member) {
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
        this.member = member;
    }
}
