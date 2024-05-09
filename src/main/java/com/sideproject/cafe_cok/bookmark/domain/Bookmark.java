package com.sideproject.cafe_cok.bookmark.domain;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Table(name = "bookmarks")
@Entity
public class Bookmark {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bookmark_folders_id")
    private BookmarkFolder bookmarkFolder;

    protected Bookmark() {
    }

    public Bookmark(final Cafe cafe, final BookmarkFolder bookmarkFolder) {
        this.cafe = cafe;
        this.bookmarkFolder = bookmarkFolder;
    }
}
