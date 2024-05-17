package com.sideproject.cafe_cok.admin.application;


import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.CafeMainImageDto;
import com.sideproject.cafe_cok.image.dto.CafeOtherImageDto;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.MenuDto;
import com.sideproject.cafe_cok.utils.FormatConverter;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.utils.Constants.*;
import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;
    private final OperationHourRepository operationHourRepository;

    private final S3Uploader s3Uploader;

    private final Integer START_TIME_IDX = 0;
    private final Integer END_TIME_IDX = 1;
    private final Boolean CAFE_IS_NOT_CLOSED = false;
    private final Boolean CAFE_IS_CLOSED = true;


    @Transactional
    public AdminCafeSaveResponse saveCafe(final AdminCafeSaveRequest request,
                                          final MultipartFile mainImage, final List<MultipartFile> otherImages) {

        Cafe cafe = request.toEntity();
        Cafe savedCafe = cafeRepository.save(cafe);
        CafeMainImageDto mainImageDto = saveCafeMainImages(mainImage, savedCafe);
        List<CafeOtherImageDto> otherImageDtos = saveCafeOtherImages(otherImages, savedCafe, mainImageDto.getOrigin());
        List<MenuDto> menuDtos = saveMenu(request.getMenus(), savedCafe);
        List<List<String>> operationHours = saveOperationHours(savedCafe, request.getHours());

        return AdminCafeSaveResponse.of(savedCafe, mainImageDto, otherImageDtos, menuDtos, operationHours);
    }

    private CafeMainImageDto saveCafeMainImages(final MultipartFile mainImage, final Cafe cafe) {
        String cafeMainOriginImageUrl = s3Uploader.upload(mainImage, CAFE_MAIN_ORIGIN_IMAGE_DIR);
        Image cafeMainOriginImage = new Image(ImageType.CAFE_MAIN_ORIGIN, cafeMainOriginImageUrl, cafe);
        Image cafeMainMediumImage = new Image(ImageType.CAFE_MAIN_MEDIUM,
                changePath(cafeMainOriginImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_MEDIUM_IMAGE_DIR), cafe);
        Image cafeMainThumbnailImage = new Image(ImageType.CAFE_MAIN_THUMBNAIL,
                changePath(cafeMainOriginImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_THUMBNAIL_DIR), cafe);

        List<Image> savedImages = imageRepository.saveAll(Arrays.asList(cafeMainOriginImage, cafeMainMediumImage, cafeMainThumbnailImage));
        return CafeMainImageDto.from(savedImages);
    }

    private List<CafeOtherImageDto> saveCafeOtherImages(final List<MultipartFile> otherImages, final Cafe cafe,
                                                        final ImageDto cafeMainOriginImage) {

        List<CafeOtherImageDto> cafeOtherImageDtos = new ArrayList<>();
        if(otherImages != null) {
            for (MultipartFile otherImage : otherImages) {
                String otherImageUrl = s3Uploader.upload(otherImage, CAFE_ORIGIN_IMAGE_DIR);
                Image cafeOriginImage = new Image(ImageType.CAFE_ORIGIN, otherImageUrl, cafe);
                Image cafeThumbnailImage = new Image(ImageType.CAFE_THUMBNAIL,
                        changePath(cafeMainOriginImage.getImageUrl(), CAFE_ORIGIN_IMAGE_DIR, CAFE_THUMBNAIL_IMAGE_DIR),
                        cafe);
                List<Image> savedImages = imageRepository.saveAll(Arrays.asList(cafeOriginImage, cafeThumbnailImage));
                cafeOtherImageDtos.add(CafeOtherImageDto.from(savedImages));
            }
        }

        return cafeOtherImageDtos;
    }

    private List<MenuDto> saveMenu(final List<AdminMenuSaveRequest> menus, final Cafe cafe) {
        List<MenuDto> menuDtos = new ArrayList<>();

        if(menus.isEmpty()) return menuDtos;

        for (AdminMenuSaveRequest menu : menus) {
            if (menu.getImage() != null) {

                File convertedFile = convertBase64StringToFile(menu.getImage());
                String originMenuUrl = s3Uploader.upload(convertedFile, MENU_ORIGIN_IMAGE_DIR);
                Menu savedMenu = menuRepository.save(menu.toEntity(cafe));
                Image originImage = new Image(ImageType.MENU_ORIGIN, originMenuUrl, cafe, savedMenu);
                Image thumbnailImage = new Image(ImageType.MENU_THUMBNAIL,
                        changePath(originMenuUrl, MENU_ORIGIN_IMAGE_DIR, MENU_THUMBNAIL_IMAGE_DIR),
                        cafe, savedMenu);

                imageRepository.saveAll(Arrays.asList(originImage, thumbnailImage));
                menuDtos.add(MenuDto.of(savedMenu, originImage, thumbnailImage));
            }
        }

        return menuDtos;
    }

    private List<List<String>> saveOperationHours(final Cafe cafe, List<List<String>> hours) {

        List<OperationHour> operationHours = new ArrayList<>();

        if(allOperatingHoursEmpty(hours)) return createEmptyOperationHours();

        for (int i = 0; i < hours.size(); i++) {
            List<String> hour = hours.get(i);

            LocalTime startTime = LocalTime.of(0, 0, 0);
            LocalTime endTime = LocalTime.of(0, 0, 0);
            boolean cafeIsClosed = CAFE_IS_CLOSED;

            if(!hour.get(START_TIME_IDX).isEmpty()) {
                startTime = LocalTime.parse(hour.get(START_TIME_IDX));
                endTime = LocalTime.parse(hour.get(END_TIME_IDX));
                cafeIsClosed = CAFE_IS_NOT_CLOSED;
            }
            operationHours.add(new OperationHour(operationDays.get(i), startTime, endTime, cafeIsClosed, cafe));
        }

        List<OperationHour> savedOperationHours = operationHourRepository.saveAll(operationHours);
        return FormatConverter.convertOperationHoursToListString(savedOperationHours);
    }

    private boolean allOperatingHoursEmpty(final List<List<String>> hours) {
        for (List<String> hour : hours) if(!hour.get(START_TIME_IDX).isEmpty()) return false;
        return true;
    }

    private List<List<String>> createEmptyOperationHours() {
        List<List<String>> emptyHours = new ArrayList<>();
        for(int i = 0; i < 7; i++) emptyHours.add(Arrays.asList("", ""));
        return emptyHours;
    }
}
