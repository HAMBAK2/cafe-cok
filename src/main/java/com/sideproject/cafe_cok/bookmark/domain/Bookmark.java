package com.sideproject.cafe_cok.bookmark.domain;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Table(name = "bookmarks")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Bookmark(final Cafe cafe,
                    final BookmarkFolder bookmarkFolder) {
        this.cafe = cafe;
        if(bookmarkFolder != null) {
            changeBookmarkFolder(bookmarkFolder);
        }
    }

    public void changeBookmarkFolder(final BookmarkFolder bookmarkFolder) {
        this.bookmarkFolder = bookmarkFolder;
        bookmarkFolder.getBookmarks().add(this);
    }


}
