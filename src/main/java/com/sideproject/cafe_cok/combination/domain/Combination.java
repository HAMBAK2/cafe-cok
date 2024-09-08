package com.sideproject.cafe_cok.combination.domain;

import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "combination", cascade = CascadeType.ALL)
    private List<CombinationKeyword> combinationKeywords = new ArrayList<>();

    @Builder
    public Combination(final Long id,
                       final String name,
                       final String icon,
                       final Member member) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        if(member != null) changeMember(member);
    }

    public void changeMember(final Member member) {
        this.member = member;
        member.getCombinations().add(this);
    }
}
