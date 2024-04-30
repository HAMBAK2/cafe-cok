package com.sideproject.hororok.combination.dto.request;

import com.sideproject.hororok.combination.domain.Combination;
import com.sideproject.hororok.member.domain.Member;
import lombok.Getter;

import java.util.List;

@Getter
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
