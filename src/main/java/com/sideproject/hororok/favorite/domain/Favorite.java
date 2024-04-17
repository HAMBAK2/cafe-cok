package com.sideproject.hororok.favorite.domain;

import com.sideproject.hororok.cafe.domain.Cafe;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Table(name = "favorites")
@Entity
public class Favorite {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bookmark_folders_id")
    private FavoriteFolder favoriteFolder;

    protected Favorite() {
    }

    public Favorite(final Cafe cafe, final FavoriteFolder favoriteFolder) {
        this.cafe = cafe;
        this.favoriteFolder = favoriteFolder;
    }
}
