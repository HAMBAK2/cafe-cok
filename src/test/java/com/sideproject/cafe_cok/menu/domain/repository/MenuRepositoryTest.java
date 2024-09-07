package com.sideproject.cafe_cok.menu.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("카페 id를 기반으로 메뉴 리스트를 조회한다.")
    void find_menu_list_by_cafe_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Menu menu1 = new Menu(MENU_NAME_1, MENU_PRICE_1, savedCafe);
        Menu menu2 = new Menu(MENU_NAME_2, MENU_PRICE_2, savedCafe);
        Menu savedMenu1 = menuRepository.save(menu1);
        Menu savedMenu2 = menuRepository.save(menu2);

        //when
        List<Menu> findMenus = menuRepository.findByCafeId(savedCafe.getId());

        //then
        assertThat(findMenus).hasSize(2);
        assertThat(findMenus).extracting("name").containsExactlyInAnyOrder(MENU_NAME_1, MENU_NAME_2);
        assertThat(findMenus).extracting("price").containsExactlyInAnyOrder(MENU_PRICE_1, MENU_PRICE_2);
    }

    @Test
    @DisplayName("카페 id를 기반으로 MenuImageUrlDto의 리스트를 조회한다.")
    void find_menu_image_url_dto_list_by_cafe_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Menu menu1 = new Menu(MENU_NAME_1, MENU_PRICE_1, savedCafe);
        Menu menu2 = new Menu(MENU_NAME_2, MENU_PRICE_2, savedCafe);
        Menu savedMenu1 = menuRepository.save(menu1);
        Menu savedMenu2 = menuRepository.save(menu2);
        Image image1 = new Image(ImageType.MENU, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe, savedMenu1);
        Image image2 = new Image(ImageType.MENU, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe, savedMenu2);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);

        //when
        List<MenuImageUrlDto> findList = menuRepository.getMenuImageUrls(savedCafe.getId());

        //then
        assertThat(findList).hasSize(2);
        assertThat(findList).extracting("name").containsExactlyInAnyOrder(MENU_NAME_1, MENU_NAME_2);
        assertThat(findList).extracting("price")
                .containsExactlyInAnyOrder(MENU_IMAGE_URL_DTO_PRICE_1, MENU_IMAGE_URL_DTO_PRICE_2);
        assertThat(findList).extracting("originUrl")
                .containsExactlyInAnyOrder(IMAGE_ORIGIN_URL_1, IMAGE_ORIGIN_URL_2);
        assertThat(findList).extracting("thumbnailUrl")
                .containsExactlyInAnyOrder(IMAGE_THUMBNAIL_URL_1, IMAGE_THUMBNAIL_URL_2);
    }
}