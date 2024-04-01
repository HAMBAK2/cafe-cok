package com.sideproject.hororok.cafe.service;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.menu.service.MenuService;
import com.sideproject.hororok.cafe.cond.CafeCategorySearchCond;
import com.sideproject.hororok.cafe.cond.CafeSearchCond;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.cafe.repository.CafeRepository;
import com.sideproject.hororok.cafeImage.service.CafeImageService;
import com.sideproject.hororok.category.service.CategoryService;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.operationHours.entity.OperationHour;
import com.sideproject.hororok.operationHours.service.OperationHourService;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.review.service.ReviewService;
import com.sideproject.hororok.reviewImage.entity.ReviewImage;
import com.sideproject.hororok.reviewImage.service.ReviewImageService;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.utils.converter.FormatConverter;
import com.sideproject.hororok.cafe.enums.OpenStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sideproject.hororok.cafe.dto.CafeReSearchDto.of;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final ReviewImageService reviewImageService;
    private final CafeImageService cafeImageService;
    private final ReviewService reviewService;
    private final CafeRepository cafeRepository;
    private final MenuService menuService;
    private final CategoryService categoryService;
    private final OperationHourService operationHourService;

    private final BigDecimal MAX_RADIUS = BigDecimal.valueOf(2000);

    @Transactional
    @LogTrace
    public CafeDetailDto findCafeDetail(Long cafeId){

        Cafe cafe =  findCafeById(cafeId);
        List<MenuDto> menus = menuService.findByCafeId(cafeId);
        List<ReviewImage> reviewImages = reviewService.findReviewImagesByCafeId(cafeId);
        List<String> cafeImageUrls = cafeImageService.findCafeImageUrlsByCafeId(cafeId);
        List<ReviewDto> reviews = reviewService.findReviewByCafeId(cafeId);

        List<String> reviewImageUrls = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImages) {
            reviewImageUrls.add(reviewImage.getImageUrl());
        }

        //리뷰중에서 태그의 개수가 많은 거 3개 뽑아야함
        List<KeywordDto> cafeKeywords = reviewService.findKeywordInReviewByCafeIdOrderByDesc(cafe.getId());
        addReviewImageUrlsToCafeImageUrls(cafeImageUrls, reviewImageUrls);

        OpenStatus openStatus = getOpenStatus(cafeId);
        List<String> closedDay = getClosedDay(cafeId);
        List<String> businessHours = getBusinessHours(cafeId);

        return CafeDetailDto.of(cafe, menus, openStatus, businessHours, closedDay, reviewImageUrls, reviews, cafeKeywords, cafeImageUrls);
    }


    @LogTrace
    private List<String> getBusinessHours(Long cafeId) {

        List<OperationHour> businessHours = operationHourService.findBusinessHoursByCafeId(cafeId);
        List<String> convertedBusinessHours = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (OperationHour businessHour : businessHours) {
            String input;
            String date = FormatConverter.getKoreanDayOfWeek(businessHour.getDate());
            String openingTime = businessHour.getOpeningTime().format(formatter);
            String closingTime = businessHour.getClosingTime().format(formatter);

            input = date + " " + openingTime + "~" + closingTime;
            convertedBusinessHours.add(input);
        }

        return convertedBusinessHours;
    }

    @LogTrace
    private OpenStatus getOpenStatus(Long cafeId){

        LocalTime time = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        OperationHour operationHour = operationHourService.findByCafeIdAndDate(cafeId, today).get();

        if(operationHour.isClosed()) return OpenStatus.HOLY_DAY;

        if(time.isAfter(operationHour.getOpeningTime()) && time.isBefore(operationHour.getClosingTime())) {
            return OpenStatus.OPEN;
        }

        return OpenStatus.OPEN;
    }

    @LogTrace
    private List<String> getClosedDay(Long cafeId) {


        List<OperationHour> findDays = operationHourService.findClosedDayByCafeId(cafeId);

        if(findDays.isEmpty()) Optional.empty();

        List<String> closeDays = new ArrayList<>();
        for (OperationHour findDay : findDays) {
            DayOfWeek date = findDay.getDate();
            closeDays.add(FormatConverter.getKoreanDayOfWeek(date));
        }

        return closeDays;
    }

    @LogTrace
    private void addReviewImageUrlsToCafeImageUrls(List<String> cafeImageUrls, List<String> reviewImageUrls){

        int idx = 0;
        while(idx < reviewImageUrls.size() && idx < 2) {
            cafeImageUrls.add(reviewImageUrls.get(idx));
            idx++;
        }
    }

    @LogTrace
    public Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new EntityNotFoundException("카페가 존재하지 않습니다."));
    }

    @LogTrace
    public Cafe findByLongitudeAndLatitude(CafeSearchCond cafeSearchCond) {
        return cafeRepository.findByLongitudeAndLatitude(cafeSearchCond.getLongitude(), cafeSearchCond.getLatitude())
                .orElseThrow(() -> new EntityNotFoundException("카페가 존재하지 않습니다."));
    }

    @LogTrace
    public CafeReSearchDto findWithinRadius(CafeSearchCond searchCond) {
        List<Cafe> cafes = findAll();
        List<WithinRadiusCafeDto> withinRadiusCafes = new ArrayList<>();
        boolean isExist = false;
        for (Cafe cafe : cafes) {
            boolean withinRadius = GeometricUtils.isWithinRadius(searchCond.getLatitude(), searchCond.getLongitude(),
                    cafe.getLatitude(), cafe.getLongitude(), MAX_RADIUS);

            if(withinRadius) {
                Optional<String> imageUrlOptional = cafeImageService.findOneImageUrlByCafeId(cafe.getId());
                String imageUrl = imageUrlOptional.orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));
                withinRadiusCafes.add(WithinRadiusCafeDto.from(cafe, imageUrl));
                isExist = true;
            }
        }


        return of(isExist, withinRadiusCafes, categoryService.findAllCategoryAndKeyword());
    }

    @LogTrace
    public CafeBarSearchDto barSearch(CafeSearchCond searchCond) {

        if (!cafeRepository.existsByLongitudeAndLatitude(searchCond.getLongitude(), searchCond.getLatitude())) {
            CafeReSearchDto withinRadius = findWithinRadius(searchCond);
            return CafeBarSearchDto.from(withinRadius);
        }

        return CafeBarSearchDto
                .from(findCafeDetail(cafeRepository
                        .findByLongitudeAndLatitude(searchCond.getLongitude(), searchCond.getLatitude()).get().getId()));
    }

    @LogTrace
    public CafeCategorySearchDto categorySearch(CafeCategorySearchCond searchCond) {
        CafeReSearchDto withinRadius = findWithinRadius(CafeSearchCond.from(searchCond));

        List<Cafe> cafeWithKeywordsInReview = reviewService.findCafeWithKeywordsInReview(searchCond);
        List<WithinRadiusCafeDto> cafes = withinRadius.getCafes();

        List<WithinRadiusCafeDto> sameCafes = cafeWithKeywordsInReview.stream()
                .map(Cafe::getId)
                .collect(Collectors.toSet())
                .stream()
                .flatMap(cafeId ->
                        cafes.stream()
                                .filter(cafe -> cafe.getId().equals(cafeId)))
                .collect(Collectors.toList());

        return CafeCategorySearchDto.builder()
                .keywordsByCategory(withinRadius.getKeywordsByCategory())
                .cafes(sameCafes)
                .build();
    }


    @LogTrace
    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    @LogTrace
    public List<Cafe> findAllByOrderByStarRatingDescNameAsc() {
        return cafeRepository.findAllByOrderByStarRatingDescNameAsc();
    }
}
