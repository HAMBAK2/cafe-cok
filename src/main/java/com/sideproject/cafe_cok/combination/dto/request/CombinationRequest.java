package com.sideproject.cafe_cok.combination.dto.request;

import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CombinationRequest {

    private String name;
    private String icon;
    private List<String> keywords;


    public CombinationRequest(final String name, final String icon, final List<String> keywords) {
        this.name = name;
        this.icon = icon;
        this.keywords = keywords;
    }

    public Combination toEntity(final Member member) {
        return new Combination(name, icon, member);
    }
}
