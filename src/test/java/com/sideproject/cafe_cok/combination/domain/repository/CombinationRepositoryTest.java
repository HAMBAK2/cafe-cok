package com.sideproject.cafe_cok.combination.domain.repository;

import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationRepository;
import com.sideproject.cafe_cok.combination.exception.NoSuchCombinationException;
import com.sideproject.cafe_cok.common.annotation.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.사용자;
import static org.assertj.core.api.Assertions.*;


class CombinationRepositoryTest extends RepositoryTest {

    @Autowired
    private CombinationRepository combinationRepository;

    @Test
    @DisplayName("조합 ID로 조합을 찾는다 - 성공")
    public void test_get_combination_by_combination_id() {

        Combination combination = new Combination("name", "icon", 사용자());
        Combination savedCombination = combinationRepository.save(combination);

        Combination findCombination = combinationRepository.getById(savedCombination.getId());

        assertThat(findCombination.getName()).isEqualTo(combination.getName());
        assertThat(findCombination.getIcon()).isEqualTo(combination.getIcon());
        assertThat(findCombination.getMember()).isEqualTo(combination.getMember());
    }

    @Test
    @DisplayName("전달받은 조합 ID에 해당하는 조합이 없으면 에러를 리턴한다.")
    public void test_return_error_when_not_exist_combination_id() {

        Long combinationId = 1L;
        assertThatThrownBy(() -> combinationRepository.getById(combinationId))
                .isInstanceOf(NoSuchCombinationException.class);
    }
}