package com.sideproject.cafe_cok.combination.domain;

import com.sideproject.cafe_cok.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "combinations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Combination {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon", nullable = false)
    private String icon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    public Combination(final String name, final String icon, final Member member) {
        this.name = name;
        this.icon = icon;
        this.member = member;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }
}
