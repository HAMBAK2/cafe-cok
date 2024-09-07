package com.sideproject.cafe_cok.menu.application;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.response.MenuIdResponse;
import com.sideproject.cafe_cok.util.S3.component.S3Uploader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static com.sideproject.cafe_cok.constant.TestConstants.MENU_PRICE_1;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private MenuService menuService;

    /** TODO: S3에 이미지 저장된거 제거 되었는지는 어떻게 확인할 수 있는지? **/
    @Test
    public void 메뉴_ID_기반_메뉴_삭제() {

        //given
        Cafe cafe = new Cafe();
        Menu menu = Menu.builder()
                .id(ENTITY_ID)
                .cafe(cafe)
                .build();
        Image image = new Image(ImageType.MENU, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, cafe, menu);

        //mocking
        given(menuRepository.getById(anyLong())).willReturn(menu);
        given(imageRepository.findByMenu(menu)).willReturn(Arrays.asList(image));
        doNothing().when(s3Uploader).delete(anyString());

        //when
        MenuIdResponse response = menuService.delete(menu.getId());

        //then
        assertThat(response.getMenuId()).isEqualTo(menu.getId());
    }


}