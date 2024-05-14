package com.sideproject.cafe_cok.admin.application;


import com.sideproject.cafe_cok.admin.domain.CafeCopy;
import com.sideproject.cafe_cok.admin.domain.MenuCopy;
import com.sideproject.cafe_cok.admin.domain.repository.MenuCopyRepository;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.domain.repository.CafeCopyRepository;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveTestRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import com.sideproject.cafe_cok.admin.domain.ImageCopy;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.admin.domain.repository.ImageCopyRepository;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.FormatConverter;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.sideproject.cafe_cok.utils.Constants.*;
import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final MenuCopyRepository menuCopyRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;


    /* TODO: 클라이언트 단에서 Naver Map API로 변경하면 COPY 테이블이 아닌 실제 테이블에 적용해야 함 */
    @Transactional
    public AdminCafeSaveResponse saveCafe(final AdminCafeSaveRequest request,
                                          final MultipartFile mainImage, final List<MultipartFile> otherImages) {

        List<Image> images = new ArrayList<>();
        //카페 저장
        String mainImageUrl = s3Uploader.upload(mainImage, CAFE_MAIN_ORIGIN_IMAGE_DIR);
        Cafe cafe = request.toEntity(mainImageUrl);
        Cafe savedCafe = cafeRepository.save(cafe);
        images.add(new Image(ImageType.CAFE_MAIN_MEDIUM,
                changePath(mainImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_MEDIUM_IMAGE_DIR),
                savedCafe));

        images.add(new Image(ImageType.CAFE_MAIN_THUMBNAIL,
                changePath(mainImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_THUMBNAIL_DIR),
                savedCafe) );

        //카페의 대표이미지와 나머지 이미지가 존재할 경우 나머지 이미지도 S3에 저장
        if(otherImages != null) {
            for (MultipartFile otherImage : otherImages) {
                String otherImageUrl = s3Uploader.upload(otherImage, CAFE_ORIGIN_IMAGE_DIR);
                images.add(new Image(ImageType.CAFE_THUMBNAIL,
                        changePath(mainImageUrl, CAFE_ORIGIN_IMAGE_DIR, CAFE_THUMBNAIL_IMAGE_DIR),
                        savedCafe));
            }
        }

        //메뉴 정보가 존재한다면 메뉴 저장
        List<AdminMenuSaveRequest> menus = request.getMenus();
        if(!menus.isEmpty()) { images.addAll(saveMenu(menus, savedCafe)); }

        imageRepository.saveAll(images);
        return AdminCafeSaveResponse.of(savedCafe);
    }

    private List<Image> saveMenu(final List<AdminMenuSaveRequest> menus, final Cafe cafe) {
        List<Image> menuThumbnailImages = new ArrayList<>();
        for (AdminMenuSaveRequest menu : menus) {
            String imageUrl = null;
            if (menu.getImage() != null) {
                File convertedFile = convertBase64StringToFile(menu.getImage());
                imageUrl = s3Uploader.upload(convertedFile, MENU_ORIGIN_IMAGE_DIR);
                menuThumbnailImages.add(new Image(ImageType.MENU_THUMBNAIL,
                        changePath(imageUrl, MENU_ORIGIN_IMAGE_DIR, MENU_THUMBNAIL_IMAGE_DIR),
                        cafe));
            }

            Menu menuEntity = menu.toEntity(imageUrl, cafe);
            menuRepository.save(menuEntity);
        }

        return menuThumbnailImages;
    }
}
