package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.exception.NoSuchCafeException;
import com.sideproject.hororok.common.annotation.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페;
import static org.assertj.core.api.Assertions.*;

class CafeRepositoryTest extends RepositoryTest {

    @Autowired
    private CafeRepository cafeRepository;

    @Test
    @DisplayName("카페Id로 카페를 조회한다.")
    public void test_get_cafe_by_id() {

        Cafe cafe = 카페();
        Cafe savedCafe = cafeRepository.save(cafe);

        Cafe findCafe = cafeRepository.getById(savedCafe.getId());

        assertThat(findCafe.getId()).isEqualTo(savedCafe.getId());
    }

    @Test
    @DisplayName("존재하지 않는 카페 ID로 조회했을 시 에러를 리턴한다.")
    public void test_return_error_not_exist_cafe_id() {

        Long notExistId = 3L;
        assertThatThrownBy(() -> cafeRepository.getById(notExistId))
                .isInstanceOf(NoSuchCafeException.class);
    }

}