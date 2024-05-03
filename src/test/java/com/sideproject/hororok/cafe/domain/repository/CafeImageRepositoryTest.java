package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeImage;
import com.sideproject.hororok.cafe.exception.NoSuchCafeImageException;
import com.sideproject.hororok.common.annotation.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페;
import static org.assertj.core.api.Assertions.*;


class CafeImageRepositoryTest extends RepositoryTest {

    @Autowired
    private CafeImageRepository cafeImageRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Test
    @DisplayName("카페 ID로 카페 이미지 URL을 조회한다.")
    public void test_get_cafe_image_URL_by_cafe_id() {

        Cafe cafe = 카페();
        Cafe savedCafe = cafeRepository.save(cafe);

        CafeImage cafeImage = new CafeImage("이미지주소", savedCafe);
        CafeImage savedCafeImage = cafeImageRepository.save(cafeImage);

        String findImageUrl = cafeImageRepository.getOneImageUrlByCafeId(savedCafe.getId());

        assertThat(findImageUrl).isEqualTo(savedCafeImage.getImageUrl());
    }

    @Test
    @DisplayName("카페 이미지 테이블에 저장되지 않은 cafeId로 검색할 시 에러를 리턴한다.")
    public void test_return_error_not_exist_cafe_id() {

        Long notExistCafeId = 3L;

        assertThatThrownBy(() -> cafeImageRepository.getOneImageUrlByCafeId(notExistCafeId))
                .isInstanceOf(NoSuchCafeImageException.class);
    }
}