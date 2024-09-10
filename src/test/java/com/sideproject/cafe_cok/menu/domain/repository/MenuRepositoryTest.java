package com.sideproject.cafe_cok.menu.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.MenuImageDto;
import com.sideproject.cafe_cok.menu.exception.NoSuchMenuException;
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
    void 메뉴_ID를_기반으로_메뉴를_조회한다(){

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();;
        Cafe savedCafe = cafeRepository.save(cafe);

        Menu menu = Menu.builder()
                .name(MENU_NAME_1)
                .price(MENU_PRICE_1)
                .cafe(savedCafe)
                .build();
        Menu savedMenu = menuRepository.save(menu);

        //when
        Menu findMenu = menuRepository.getById(savedMenu.getId());

        //then
        assertThat(findMenu.getName()).isEqualTo(MENU_NAME_1);
        assertThat(findMenu.getPrice()).isEqualTo(MENU_PRICE_1);
        assertThat(findMenu.getCafe()).isEqualTo(savedCafe);
    }

    @Test
    void 존재하지_않는_메뉴_ID로_메뉴_조회_시_에러를_발생시킨다() {

        //when & then
        assertThatExceptionOfType(NoSuchMenuException.class)
                .isThrownBy(() -> menuRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 메뉴가 존재하지 않습니다.");
    }


    @Test
    void 카페_ID를_기반으로_메뉴_리스트를_조회한다() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();;
        Cafe savedCafe = cafeRepository.save(cafe);

        Menu menu1 = Menu.builder()
                .name(MENU_NAME_1)
                .price(MENU_PRICE_1)
                .cafe(savedCafe)
                .build();
        Menu savedMenu1 = menuRepository.save(menu1);

        Menu menu2 = Menu.builder()
                .name(MENU_NAME_2)
                .price(MENU_PRICE_2)
                .cafe(savedCafe)
                .build();
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
    void 카페_ID를_기반으로_MENU_IMAGE_URL_DTO_리스트를_조회한다() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();;
        Cafe savedCafe = cafeRepository.save(cafe);

        Menu menu1 = Menu.builder()
                .name(MENU_NAME_1)
                .price(MENU_PRICE_1)
                .cafe(savedCafe)
                .build();
        Menu savedMenu1 = menuRepository.save(menu1);

        Menu menu2 = Menu.builder()
                .name(MENU_NAME_2)
                .price(MENU_PRICE_2)
                .cafe(savedCafe)
                .build();
        Menu savedMenu2 = menuRepository.save(menu2);

        Image image1 =  Image.builder()
                .imageType(ImageType.MENU)
                .origin(IMAGE_ORIGIN_URL_1)
                .thumbnail(IMAGE_THUMBNAIL_URL_1)
                .cafe(savedCafe)
                .menu(savedMenu1)
                .build();
        Image savedImage1 = imageRepository.save(image1);

        Image image2 = Image.builder()
                .imageType(ImageType.MENU)
                .origin(IMAGE_ORIGIN_URL_2)
                .thumbnail(IMAGE_THUMBNAIL_URL_2)
                .cafe(savedCafe)
                .menu(savedMenu2)
                .build();
        Image savedImage2 = imageRepository.save(image2);

        //when
        List<MenuImageDto> findList = menuRepository.getMenuImageUrls(savedCafe.getId());

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