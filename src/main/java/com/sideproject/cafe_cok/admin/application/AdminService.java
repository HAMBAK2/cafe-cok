package com.sideproject.cafe_cok.admin.application;

import com.sideproject.cafe_cok.admin.dto.*;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeOperationHourDto;
import com.sideproject.cafe_cok.cafe.dto.CafeAdminDto;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.member.domain.Feedback;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import com.sideproject.cafe_cok.member.domain.repository.FeedbackRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.MenuDetailDto;
import com.sideproject.cafe_cok.util.FormatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final FeedbackRepository feedbackRepository;
    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;
    private final OperationHourRepository operationHourRepository;

    public List<AdminSuggestionDto> findFeedbackByCategory(final FeedbackCategory feedbackCategory) {

        List<Feedback> findFeedbacks = feedbackRepository.findByCategoryOrderByIdDesc(feedbackCategory);
        return findFeedbacks.stream()
                .map(feedback -> new AdminSuggestionDto(feedback))
                .collect(Collectors.toList());
    }

    public List<CafeAdminDto> findCafes() {

        List<Cafe> findCafes = cafeRepository.findAllByOrderByIdDesc();
        return findCafes.stream()
                .map(cafe -> new CafeAdminDto(cafe))
                .collect(Collectors.toList());
    }

    public CafeAdminDto findCafeById(final Long id) {
        Cafe findCafe = cafeRepository.getById(id);
        List<Image> filteredImage = findCafe.getImages().stream()
                .filter(image -> image.getImageType().equals(ImageType.CAFE) || image.getImageType().equals(ImageType.CAFE_MAIN))
                .collect(Collectors.toList());
        List<ImageDto> images = ImageDto.fromList(filteredImage);

        List<MenuDetailDto> findMenus = menuRepository.findByCafeId(id).stream()
                .map(menu -> {
                    List<Image> findMenuImages = imageRepository.findByMenu(menu);
                    if (findMenuImages.isEmpty()) return menu.toMenuDetailDto();
                    return menu.toMenuDetailDto(ImageDto.from(findMenuImages.get(0)));
                }).collect(Collectors.toList());


        List<OperationHour> findOperationHourList = operationHourRepository.findByCafeId(findCafe.getId());
        List<CafeOperationHourDto> hours = generateHours();
        for (CafeOperationHourDto hour : hours) {
            String day = hour.getDay();
            for (OperationHour operationHour : findOperationHourList) {
                if(day.equals(FormatConverter.getKoreanDayOfWeek(operationHour.getDate()))) {
                    hour.changeTime(operationHour);
                }
            }
        }

        return new CafeAdminDto(findCafe, images, findMenus, hours);
    }

    private List<CafeOperationHourDto> generateHours() {

        List<String> daysOfWeek = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        List<CafeOperationHourDto> hours = new ArrayList<>();
        for (String day : daysOfWeek) {
            hours.add(new CafeOperationHourDto(day));
        }

        return hours;
    }
}
