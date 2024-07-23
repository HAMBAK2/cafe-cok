package com.sideproject.cafe_cok.admin.application;

import com.sideproject.cafe_cok.admin.dto.*;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeUpdateRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuRequestDto;
import com.sideproject.cafe_cok.admin.dto.response.AdminSuccessAndRedirectResponse;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.member.domain.Feedback;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import com.sideproject.cafe_cok.member.domain.repository.FeedbackRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.utils.FormatConverter;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.utils.Constants.*;
import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService2 {

    private final FeedbackRepository feedbackRepository;
    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;
    private final OperationHourRepository operationHourRepository;
    private final S3Uploader s3Uploader;

    public List<AdminSuggestionDto> findFeedbackByCategory(final FeedbackCategory feedbackCategory) {

        List<Feedback> findFeedbacks = feedbackRepository.findByCategoryOrderByIdDesc(feedbackCategory);
        return findFeedbacks.stream()
                .map(feedback -> new AdminSuggestionDto(feedback))
                .collect(Collectors.toList());
    }

    public List<AdminCafeDto> findCafes() {

        List<Cafe> findCafes = cafeRepository.findAllByOrderByIdDesc();
        return findCafes.stream()
                .map(cafe -> new AdminCafeDto(cafe))
                .collect(Collectors.toList());
    }

    public AdminCafeDto findCafeById(final Long id) {
        Cafe findCafe = cafeRepository.getById(id);
        List<Image> filteredImage = findCafe.getImages().stream()
                .filter(image -> image.getImageType().equals(ImageType.CAFE) || image.getImageType().equals(ImageType.CAFE_MAIN))
                .collect(Collectors.toList());
        List<ImageDto> images = ImageDto.fromList(filteredImage);

        List<AdminMenuDto> findMenus = menuRepository.findByCafeId(id).stream()
                .map(menu -> {
                    List<Image> findMenuImages = imageRepository.findByMenu(menu);
                    if (findMenuImages.isEmpty()) return new AdminMenuDto(menu);
                    return new AdminMenuDto(menu, ImageDto.from(findMenuImages.get(0)));
                }).collect(Collectors.toList());


        List<OperationHour> findOperationHourList = operationHourRepository.findByCafeId(findCafe.getId());
        List<AdminOperationHourDto> hours = generateHours();
        for (AdminOperationHourDto hour : hours) {
            String day = hour.getDay();
            for (OperationHour operationHour : findOperationHourList) {
                if(day.equals(FormatConverter.getKoreanDayOfWeek(operationHour.getDate()))) {
                    hour.changeTime(operationHour);
                }
            }
        }

        return new AdminCafeDto(findCafe, images, findMenus, hours);
    }

    @Transactional
    public AdminSuccessAndRedirectResponse updateCafe(final Long id,
                                                      final AdminCafeUpdateRequest request) {

        Cafe findCafe = cafeRepository.getById(id);
        findCafe.setName(request.getName());
        findCafe.setRoadAddress(request.getAddress());
        findCafe.setPhoneNumber(request.getPhoneNumber());
        cafeRepository.save(findCafe);


        //카페 메인 이미지 수정
        if(request.getImage().getImageBase64() != null) {

            File converted = convertBase64StringToFile(request.getImage().getImageBase64());
            String originImageUrl = s3Uploader.upload(converted, CAFE_MAIN_ORIGIN_IMAGE_DIR);

            Optional<Image> optionalImage = imageRepository.findById(request.getImage().getId());
            if(optionalImage.isPresent()) {
                Image findImage = optionalImage.get();
                s3Uploader.delete(findImage.getOrigin());
                s3Uploader.delete(findImage.getMedium());
                s3Uploader.delete(findImage.getThumbnail());
                findImage.changeOrigin(originImageUrl);
                findImage.changMedium(changePath(originImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_MEDIUM_IMAGE_DIR));
                findImage.changeThumbnail(changePath(originImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_THUMBNAIL_DIR));
                imageRepository.save(findImage);
            }
        }

        //카페 나머지 이미지 수정
        for (AdminImageDto otherImage : request.getOtherImages()) {

            File converted = convertBase64StringToFile(otherImage.getImageBase64());
            String originImageUrl = s3Uploader.upload(converted, CAFE_ORIGIN_IMAGE_DIR);
            String thumbnailImageUrl = changePath(originImageUrl, CAFE_ORIGIN_IMAGE_DIR, CAFE_THUMBNAIL_IMAGE_DIR);
            //기존 이미지가 수정되는 경우
            if(otherImage.getId() != null) {
                Optional<Image> optionalImage = imageRepository.findById(otherImage.getId());
                if(optionalImage.isPresent()) {
                    Image findImage = optionalImage.get();
                    s3Uploader.delete(findImage.getThumbnail());
                    s3Uploader.delete(findImage.getOrigin());
                    findImage.changeOrigin(originImageUrl);
                    findImage.changeThumbnail(thumbnailImageUrl);
                    imageRepository.save(findImage);
                }
                continue;
            }

            //새로운 이미지가 추가된 경우
            Image newImage = new Image(ImageType.CAFE, originImageUrl, thumbnailImageUrl, findCafe);
            imageRepository.save(newImage);
        }

        //운영 시간 수정
        List<AdminOperationHourDto> hours = request.getHours();
        List<OperationHour> newOperationHours = new ArrayList<>();
        operationHourRepository.deleteByCafeId(id);
        if(checkoutInputHours(hours)) {
            for (AdminOperationHourDto hour : hours) {
                DayOfWeek day = getDyaOfWeekByKoreanDay(hour.getDay());
                LocalTime startTime = LocalTime.of(hour.getStartHour(), hour.getStartMinute());
                LocalTime endTime = LocalTime.of(hour.getEndHour(), hour.getEndMinute());
                boolean isClosed = false;

                if(startTime.equals(LocalTime.MIDNIGHT) && endTime.equals(LocalTime.MIDNIGHT)) isClosed = true;
                OperationHour newOperationHour = new OperationHour(day, startTime, endTime, isClosed, findCafe);
                newOperationHours.add(newOperationHour);
            }

            operationHourRepository.saveAll(newOperationHours);
        }


        //메뉴 수정
        List<AdminMenuRequestDto> menus = request.getMenus();
        for (AdminMenuRequestDto menu : menus) {

            //기존 메뉴인 경우
            Menu targetMenu;
            if(menu.getId() != null) {
                Optional<Menu> optionalMenu = menuRepository.findById(menu.getId());
                if(optionalMenu.isEmpty()) continue;
                targetMenu = optionalMenu.get();
                targetMenu.changeName(menu.getName());
                targetMenu.changePrice(menu.getPrice());
            }
            else  targetMenu = new Menu(menu.getName(), menu.getPrice(), findCafe);
            menuRepository.save(targetMenu);

            //변경하려는 이미지가 존재하는 경우
            if (menu.getImage() != null && !menu.getImage().isEmpty()) {

                File converted = convertBase64StringToFile(menu.getImage());
                String originImageUrl = s3Uploader.upload(converted, MENU_ORIGIN_IMAGE_DIR);
                String thumbnailImageUrl = changePath(originImageUrl, MENU_ORIGIN_IMAGE_DIR, MENU_THUMBNAIL_IMAGE_DIR);

                List<Image> findImages = imageRepository.findByMenu(targetMenu);
                Image targetImage;
                if(findImages.isEmpty()) {
                    targetImage = new Image(ImageType.MENU, originImageUrl, thumbnailImageUrl, findCafe, targetMenu);
                } else {
                    targetImage = findImages.get(0);
                    targetImage.changeOrigin(originImageUrl);
                    targetImage.changeThumbnail(thumbnailImageUrl);
                }

                imageRepository.save(targetImage);
            }
        }

        return new AdminSuccessAndRedirectResponse("Update successful", "/admin/cafes/" + id);
    }

    @Transactional
    public boolean menuDelete(final Long id) {

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

    private boolean checkoutInputHours(List<AdminOperationHourDto> hours) {

        for (AdminOperationHourDto hour : hours) {
            if(hour.getStartHour() != 0) return true;
            if(hour.getStartMinute() != 0) return true;
            if(hour.getEndHour() != 0) return true;
            if(hour.getEndMinute() != 0) return true;
        }

        return false;
    }

    private List<AdminOperationHourDto> generateHours() {

        List<String> daysOfWeek = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        List<AdminOperationHourDto> hours = new ArrayList<>();
        for (String day : daysOfWeek) {
            hours.add(new AdminOperationHourDto(day));
        }

        return hours;
    }

}
