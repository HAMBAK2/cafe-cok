package com.sideproject.hororok.review.Entity;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.image.entity.Image;
import com.sideproject.hororok.keword.entity.Keyword;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    private String reviewContent;

    @Column(length = 1000)
    private String specialNotes;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;

    @OneToMany(mappedBy = "review")
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "REVIEW_KEYWORD",
            joinColumns = @JoinColumn(name = "REVIEW_ID"),
            inverseJoinColumns = @JoinColumn(name = "KEYWORD_ID")
    )
    private List<Keyword> keywords = new ArrayList<>();
}
