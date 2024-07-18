package com.sideproject.cafe_cok.theme.domain;

import com.sideproject.cafe_cok.keword.domain.Keyword;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@Table(name = "themeKeywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThemeKeyword {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "keywords_id")
    private Keyword keyword;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "themes_id")
    private Theme theme;
}
