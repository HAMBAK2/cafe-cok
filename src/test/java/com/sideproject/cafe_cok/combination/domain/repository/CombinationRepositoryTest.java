package com.sideproject.cafe_cok.combination.domain.repository;

import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.combination.exception.NoSuchCombinationException;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class CombinationRepositoryTest {

    @Autowired
    private CombinationRepository combinationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("저장된 조합이 조합 ID로 정상적으로 조회되는지 확인")
    void get_combination_O() {

        //given
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Combination combination = Combination.builder()
                .name(COMBINATION_NAME_1)
                .icon(COMBINATION_ICON_1)
                .member(savedMember)
                .build();
        Combination savedCombination = combinationRepository.save(combination);

        //when
        Combination findCombination = combinationRepository.getById(savedCombination.getId());

        //then
        assertThat(savedCombination).isEqualTo(findCombination);
        assertThat(savedCombination.getName()).isEqualTo(findCombination.getName());
        assertThat(savedCombination.getIcon()).isEqualTo(findCombination.getIcon());
        assertThat(savedCombination.getMember()).isEqualTo(findCombination.getMember());
    }

    @Test
    @DisplayName("존재하지 않는 조합 ID로 조회 했을 시 에러를 발생시키는지 확인")
    void get_combination_X() {

        //when & then
        assertThatExceptionOfType(NoSuchCombinationException.class)
                .isThrownBy(() -> combinationRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 조합이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("memberId를 기반으로 조합의 목록을 조회한다.")
    void find_combination_list_by_member_id_O() {

        //given
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);

        Combination combination1 = Combination.builder()
                .name(COMBINATION_NAME_1)
                .icon(COMBINATION_ICON_1)
                .member(savedMember)
                .build();
        combinationRepository.save(combination1);

        Combination combination2 = Combination.builder()
                .name(COMBINATION_NAME_2)
                .icon(COMBINATION_ICON_2)
                .member(savedMember)
                .build();
        combinationRepository.save(combination2);

        //when
        List<CombinationDto> findCombinations = combinationRepository.findByMemberId(savedMember.getId());

        //then
        assertThat(findCombinations).hasSize(2);
        assertThat(findCombinations).extracting("name").containsExactlyInAnyOrder(COMBINATION_NAME_1, COMBINATION_NAME_2);
        assertThat(findCombinations).extracting("icon").containsExactlyInAnyOrder(COMBINATION_ICON_1, COMBINATION_ICON_2);
    }

    @Test
    @DisplayName("존재하지 않는 memberId로 조합의 목록을 조회할 시 빈 리스트를 반환한다.")
    void find_combination_list_by_non_existent_member_id() {

        //when
        List<CombinationDto> findCombinations = combinationRepository.findByMemberId(NON_EXISTENT_ID);

        //then
        assertThat(findCombinations).isEmpty();
    }
}