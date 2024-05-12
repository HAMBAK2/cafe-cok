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
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.admin.domain.repository.ImageCopyRepository;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final CafeCopyRepository cafeCopyRepository;
    private final MenuCopyRepository menuCopyRepository;
    private final ImageCopyRepository imageCopyRepository;
    private final S3Uploader s3Uploader;

    private final String CAFE_ORIGIN_IMAGE_DIR = "cafe";
    private final String MENU_ORIGIN_IMAGE_DIR = "menu";

    @Transactional
    public AdminCafeSaveResponse saveCafe(final AdminCafeSaveRequest request,
                                          final MultipartFile mainImage, final List<MultipartFile> otherImages) {

        //카페 메인 이미지 저장 후 카페 저장
        String mainImageUrl = s3Uploader.upload(mainImage, CAFE_ORIGIN_IMAGE_DIR).replace(Constants.IMAGE_URL_PREFIX, "");
        CafeCopy cafeCopy = request.toEntity(mainImageUrl);
        CafeCopy savedCafeCopy = cafeCopyRepository.save(cafeCopy);

        //카페의 나머지 이미지가 존재할 경우 나머지 이미지도 S3에 저장
        if(!otherImages.isEmpty()) {
            for (MultipartFile otherImage : otherImages) {
                String otherImageUrl = s3Uploader.upload(otherImage, CAFE_ORIGIN_IMAGE_DIR).replace(Constants.IMAGE_URL_PREFIX, "");
                ImageCopy imageCopy = new ImageCopy(ImageType.CAFE_IMAGE, otherImageUrl, savedCafeCopy);
                imageCopyRepository.save(imageCopy);
            }
        }

        //메뉴 정보가 존재한다면 메뉴 저장
        List<AdminMenuSaveRequest> menus = request.getMenus();
        if(!menus.isEmpty()) {

            for (AdminMenuSaveRequest menu : menus) {

                //메뉴의 이미지가 있는 경우 - 메뉴 이미지를 S3에 저장하고 url 할당
                String imageUrl = null;
                if (menu.getImage() != null) {
                    File convertedFile = convertBase64StringToFile(menu.getImage());
                    imageUrl = s3Uploader.upload(convertedFile, MENU_ORIGIN_IMAGE_DIR);
                }

                MenuCopy menuCopy = menu.toEntity(imageUrl, savedCafeCopy);
                menuCopyRepository.save(menuCopy);
            }
        }

        return AdminCafeSaveResponse.of(savedCafeCopy);
    }
}
