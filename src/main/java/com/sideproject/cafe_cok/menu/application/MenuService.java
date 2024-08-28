package com.sideproject.cafe_cok.menu.application;

import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.response.MenuListResponse;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public MenuListResponse findByCafeId(final Long cafeId) {

        List<MenuImageUrlDto> findMenus = menuRepository.findMenuImageUrlDtoListByCafeId(cafeId);
        return new MenuListResponse(findMenus);
    }

    @Transactional
    public boolean delete(final Long id) {

        Optional<Menu> optionalMenu = menuRepository.findById(id);
        if(optionalMenu.isEmpty()) return true;

        Menu findMenu = optionalMenu.get();
        List<Image> findMenuImages = imageRepository.findByMenu(findMenu);
        for (Image findMenuImage : findMenuImages) {
            s3Uploader.delete(findMenuImage.getThumbnail());
            s3Uploader.delete(findMenuImage.getOrigin());
        }
        imageRepository.deleteAll(findMenuImages);
        menuRepository.delete(findMenu);
        return true;
    }
}
