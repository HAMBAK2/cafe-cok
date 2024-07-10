package com.sideproject.cafe_cok.admin.application;


import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeExistResponse;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeFindResponse;
import com.sideproject.cafe_cok.admin.dto.response.AdminRestoreMemberResponse;
import com.sideproject.cafe_cok.admin.exception.NoWithdrawalMemberException;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.CafeMainImageDto;
import com.sideproject.cafe_cok.image.dto.CafeOtherImageDto;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.CafeSaveMenuDto;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.utils.FormatConverter;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.sideproject.cafe_cok.utils.Constants.*;
import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;
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
                                          final MultipartFile mainImage,
                                          final List<MultipartFile> otherImages) {

        Cafe cafe = request.toEntity();
        Optional<Cafe> findOptionalCafe = cafeRepository.findByLatitudeAndLongitude(request.getMapy(), request.getMapx());
        if(findOptionalCafe.isPresent()) {

            cafe = findOptionalCafe.get();
            List<Image> findImages = imageRepository.findImageByCafe(cafe);
            List<Menu> findMenus = menuRepository.findByCafeId(cafe.getId());

            for (Menu menu : findMenus) {
                List<Image> findMenuImages = imageRepository.findByMenu(menu);
                if(findMenuImages.isEmpty()) continue;
                findImages.add(findImages.get(0));
            }

            for (Image findImage : findImages) {
                String origin = findImage.getOrigin();
                String thumbnail = findImage.getThumbnail();
                String medium = findImage.getMedium();

                if(origin != null) s3Uploader.delete(origin);
                if(thumbnail != null) s3Uploader.delete(origin);
                if(medium != null) s3Uploader.delete(origin);
            }

            imageRepository.deleteAll(findImages);
            menuRepository.deleteAll(findMenus);
            List<OperationHour> findOperationHours = operationHourRepository.findByCafeId(cafe.getId());
            operationHourRepository.deleteAll(findOperationHours);
            cafe.changeCafe(request.getName(), request.getTelephone(), request.getRoadAddress(), request.getMapx(), request.getMapy());
        }

        Cafe savedCafe = cafeRepository.save(cafe);
        CafeMainImageDto mainImageDto = saveCafeMainImages(mainImage, savedCafe);
        List<CafeOtherImageDto> otherImageDtos = saveCafeOtherImages(otherImages, savedCafe);
        List<CafeSaveMenuDto> menuDtos = saveMenu(request.getMenus(), savedCafe);
        List<List<String>> operationHours = saveOperationHours(savedCafe, request.getHours());

        return AdminCafeSaveResponse.of(savedCafe, mainImageDto, otherImageDtos, menuDtos, operationHours);
    }

    @Transactional
    public AdminRestoreMemberResponse restoreMember(final Long memberId) {

        Member findMember = memberRepository.getById(memberId);
        if (findMember.getDeletedAt() == null) throw new NoWithdrawalMemberException();

        findMember.changeDeletedAt(null);

        List<Review> findReviews = findMember.getReviews();
        findReviews.stream()
                .forEach(review -> review.getCafe().addReviewCountAndCalculateStarRating(review.getStarRating()));

        return new AdminRestoreMemberResponse(findMember);
    }

    public AdminCafeExistResponse checkCafeExist(final BigDecimal mapx,
                                                 final BigDecimal mapy) {

        Optional<Cafe> findOptionalCafe = cafeRepository.findByLatitudeAndLongitude(mapy, mapx);
        if(findOptionalCafe.isEmpty()) return new AdminCafeExistResponse(false);
        return new AdminCafeExistResponse(true);
    }

    public AdminCafeFindResponse findCafe(final BigDecimal mapx,
                                          final BigDecimal mapy) {

        Optional<Cafe> findOptionalCafe = cafeRepository.findByLatitudeAndLongitude(mapy, mapx);
        if(findOptionalCafe.isEmpty()) return new AdminCafeFindResponse();

        Cafe findCafe = findOptionalCafe.get();
        List<Image> findCafeImages = imageRepository.findImageByCafe(findCafe);
        CafeMainImageDto findCafeMainImage = null;
        List<CafeOtherImageDto> findCafeOtherImages = new ArrayList<>();
        for (Image findCafeImage : findCafeImages) {
            if(findCafeImage.getImageType().equals(ImageType.CAFE_MAIN)) {
                findCafeMainImage = CafeMainImageDto.from(findCafeImage);
                continue;
            }
            if(findCafeImage.getImageType().equals(ImageType.CAFE)) {
                findCafeOtherImages.add(CafeOtherImageDto.from(findCafeImage));
            }
        }

        List<CafeSaveMenuDto> findMenuDtoList = new ArrayList<>();
        List<Menu> findMenus = menuRepository.findByCafeId(findCafe.getId());
        for (Menu findMenu : findMenus) {
            List<Image> findMenuImages = imageRepository.findByMenu(findMenu);
            if(findMenuImages.isEmpty()) findMenuDtoList.add(CafeSaveMenuDto.from(findMenu));
            for (Image findMenuImage : findMenuImages) {
                findMenuDtoList.add(CafeSaveMenuDto.of(findMenu, findMenuImage));
            }
        }

        List<OperationHour> findOperationHourList = operationHourRepository.findByCafeId(findCafe.getId());
        List<List<String>> findHours = convertOperationHoursToListString(findOperationHourList);
        AdminCafeSaveResponse cafe = AdminCafeSaveResponse
                .of(findCafe, findCafeMainImage, findCafeOtherImages, findMenuDtoList, findHours);

        return new AdminCafeFindResponse(cafe);
    }

    private CafeMainImageDto saveCafeMainImages(final MultipartFile mainImage, final Cafe cafe) {
        String cafeMainOriginImageUrl = s3Uploader.upload(mainImage, CAFE_MAIN_ORIGIN_IMAGE_DIR);
        Image cafeMainImage = new Image(
                ImageType.CAFE_MAIN
                , cafeMainOriginImageUrl
                , changePath(cafeMainOriginImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_THUMBNAIL_DIR)
                , changePath(cafeMainOriginImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_MEDIUM_IMAGE_DIR)
                , cafe);

        Image savedImage = imageRepository.save(cafeMainImage);
        return CafeMainImageDto.from(savedImage);
    }

    private List<CafeOtherImageDto> saveCafeOtherImages(final List<MultipartFile> otherImages, final Cafe cafe) {

        List<CafeOtherImageDto> cafeOtherImageDtos = new ArrayList<>();
        if(otherImages != null) {
            for (MultipartFile otherImage : otherImages) {
                String otherImageOriginUrl = s3Uploader.upload(otherImage, CAFE_ORIGIN_IMAGE_DIR);
                Image cafeImage = new Image(ImageType.CAFE
                        , otherImageOriginUrl
                        , changePath(otherImageOriginUrl, CAFE_ORIGIN_IMAGE_DIR, CAFE_THUMBNAIL_IMAGE_DIR)
                        , cafe);
                Image savedImage = imageRepository.save(cafeImage);
                cafeOtherImageDtos.add(CafeOtherImageDto.from(savedImage));
            }
        }

        return cafeOtherImageDtos;
    }

    private List<CafeSaveMenuDto> saveMenu(final List<AdminMenuSaveRequest> menus, final Cafe cafe) {
        List<CafeSaveMenuDto> menuDtos = new ArrayList<>();

        if(menus.isEmpty()) return menuDtos;

        for (AdminMenuSaveRequest menu : menus) {
            Menu savedMenu = menuRepository.save(menu.toEntity(cafe));

            if (menu.getImage() != null && !menu.getImage().isEmpty()) {

                File convertedFile = convertBase64StringToFile(menu.getImage());
                String originMenuUrl = s3Uploader.upload(convertedFile, MENU_ORIGIN_IMAGE_DIR);
                Image menuImage = new Image(ImageType.MENU
                        , originMenuUrl
                        , changePath(originMenuUrl, MENU_ORIGIN_IMAGE_DIR, MENU_THUMBNAIL_IMAGE_DIR)
                        , cafe
                        , savedMenu);
                Image saveMenuImage = imageRepository.save(menuImage);
                menuDtos.add(CafeSaveMenuDto.of(savedMenu, saveMenuImage));
                continue;
            }
            menuDtos.add(CafeSaveMenuDto.from(savedMenu));
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
