package com.sideproject.cafe_cok.combination.domain;

import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Combination(final String name,
                       final String icon,
                       final Member member) {
        this.name = name;
        this.icon = icon;
        if(member != null) changeMember(member);
    }

    public void changeByRequest(final CombinationRequest request) {
        this.name = request.getName();
        this.icon = request.getIcon();
    }

    public void changeMember(final Member member) {
        this.member = member;
        member.getCombinations().add(this);
    }
}
