package com.sideproject.cafe_cok.nickname.application;

import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.nickname.domain.NicknameComponent;
import com.sideproject.cafe_cok.nickname.domain.repository.NicknameComponentRepository;
import com.sideproject.cafe_cok.nickname.enums.NicknameComponentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NicknameService {

    private final NicknameComponentRepository nicknameComponentRepository;
    private final MemberRepository memberRepository;

    public String generateNickname() {

        List<NicknameComponent> allNicknameComponents = nicknameComponentRepository.findAll();
        List<NicknameComponent> adjectives = allNicknameComponents.stream()
                .filter(nicknameComponent -> nicknameComponent.getType().equals(NicknameComponentType.ADJECTIVE))
                .collect(Collectors.toList());
        List<NicknameComponent> nouns = allNicknameComponents.stream()
                .filter(nicknameComponent -> nicknameComponent.getType().equals(NicknameComponentType.NOUN))
                .collect(Collectors.toList());

        String nickname = "";
        do {

            String adjective = adjectives.get((int) Math.random() * adjectives.size()).getValue();
            String noun = nouns.get((int) Math.random() * nouns.size()).getValue();

            nickname = adjective + " " + noun;
        } while(memberRepository.existsByNickname(nickname));

        return nickname;
    }
}
