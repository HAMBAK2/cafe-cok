package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.dto.response.CafeFindAgainResponse;
import com.sideproject.hororok.cafe.dto.response.CafeFindBarResponse;
import com.sideproject.hororok.category.dto.CategoryKeywords;
import com.sideproject.hororok.keword.entity.Keyword;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.menu.service.MenuService;
import com.sideproject.hororok.cafe.cond.CafeCategorySearchCond;
import com.sideproject.hororok.cafe.cond.CafeSearchCond;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.cafeImage.service.CafeImageService;
import com.sideproject.hororok.category.service.CategoryService;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.operationHours.entity.OperationHour;
import com.sideproject.hororok.operationHours.service.OperationHourService;
import com.sideproject.hororok.review.Entity.Review;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.review.service.ReviewService;
import com.sideproject.hororok.reviewImage.entity.ReviewImage;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import com.sideproject.hororok.utils.converter.FormatConverter;
import com.sideproject.hororok.cafe.domain.OpenStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.sideproject.hororok.cafe.dto.CafeReSearchDto.of;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final CafeImageService cafeImageService;
    private final ReviewService reviewService;
    private final CafeRepository cafeRepository;
    private final MenuService menuService;
    private final CategoryService categoryService;
    private final OperationHourService operationHourService;

    private final BigDecimal MAX_RADIUS = BigDecimal.valueOf(2000);

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
    public CafeFindAgainResponse findCafeAgainByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude) {

        List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
        CategoryKeywords categoryKeywords = categoryService.findAllCategoryAndKeyword();

        return CafeFindAgainResponse.of(withinRadiusCafes, categoryKeywords);
    }

    @LogTrace
    public CafeFindBarResponse findCafeBarByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude) {

        Optional<Cafe> findCafe = cafeRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(findCafe.isEmpty()) {
            List<WithinRadiusCafe> withinRadiusCafes = findWithinRadiusCafes(latitude, longitude);
            CategoryKeywords categoryKeywords = categoryService.findAllCategoryAndKeyword();
            return CafeFindBarResponse.notExistOf(withinRadiusCafes, categoryKeywords);
        }

        return CafeFindBarResponse
                .existFrom(findCafeDetailByCafeId(findCafe.get().getId()));
    }

    @LogTrace
    public Cafe findByLongitudeAndLatitude(CafeSearchCond cafeSearchCond) {
        return cafeRepository.findByLatitudeAndLongitude(cafeSearchCond.getLongitude(), cafeSearchCond.getLatitude())
                .orElseThrow(() -> new EntityNotFoundException("카페가 존재하지 않습니다."));
    }

    @LogTrace
    public CafeDetail findCafeDetailByCafeId(Long cafeId){

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

        //유저들이 뽑은 키워드 내용 추가


        return new CafeDetail(
                cafe, menus, openStatus, businessHours, closedDay,
                reviewImageUrls, reviews, cafeKeywords, cafeImageUrls);
    }


    @LogTrace
    public List<WithinRadiusCafe> findWithinRadiusCafes(BigDecimal latitude, BigDecimal longitude) {
        List<Cafe> cafes = findAll();
        List<WithinRadiusCafe> withinRadiusCafes = new ArrayList<>();

        for (Cafe cafe : cafes) {
            boolean isWithinRadius = GeometricUtils
                    .isWithinRadius(
                            latitude, longitude,
                            cafe.getLatitude(), cafe.getLongitude(), MAX_RADIUS);

            if(isWithinRadius) {
                String cafeImageUrl =
                        cafeImageService.findOneImageUrlByCafeId(cafe.getId())
                                .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));
                withinRadiusCafes.add(WithinRadiusCafe.of(cafe, cafeImageUrl));
            }
        }

        return withinRadiusCafes;
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

        if (!cafeRepository.existsByLatitudeAndLongitude(searchCond.getLongitude(), searchCond.getLatitude())) {
            CafeReSearchDto withinRadius = findWithinRadius(searchCond);
            return CafeBarSearchDto.from(withinRadius);
        }

        return CafeBarSearchDto
                .from(findCafeDetail(cafeRepository
                        .findByLatitudeAndLongitude(searchCond.getLongitude(), searchCond.getLatitude()).get().getId()));
    }

    @LogTrace
    public CafeCategorySearchDto categorySearch(CafeCategorySearchCond searchCond) {
        CafeReSearchDto withinRadius = findWithinRadius(CafeSearchCond.from(searchCond));

        List<String> targetKeywordNames = getKeywordNamesListFromCategoryKeywords(searchCond.getCategoryKeywords());
        List<WithinRadiusCafeDto> withinRadiusCafeDtos = new ArrayList<>();


        List<WithinRadiusCafeDto> withinRadiusCafes = withinRadius.getCafes();
        for (WithinRadiusCafeDto withinRadiusCafe : withinRadiusCafes) {
            Cafe cafe = cafeRepository.findById(withinRadiusCafe.getId()).get();
            List<Review> reviews = cafe.getReviews();
            Set<String> keywordNames = new HashSet<>();
            for (Review review : reviews) {
                keywordNames.addAll(getKeywordNamesListFromKeywords(review.getKeywords()));
            }

            boolean allMatch = targetKeywordNames.stream()
                    .allMatch(keywordNames::contains);

            if(allMatch) withinRadiusCafeDtos.add(withinRadiusCafe);
        }

        return CafeCategorySearchDto.builder()
                .categoryKeywords(withinRadius.getCategoryKeywords())
                .cafes(withinRadiusCafeDtos)
                .build();
    }

    @LogTrace
    public List<String> getKeywordNamesListFromKeywords(List<Keyword> keywords) {

        List<String> keywordNames = new ArrayList<>();
        for (Keyword keyword : keywords) {
            keywordNames.add(keyword.getName());
        }

        return keywordNames;
    }

    @LogTrace
    public List<String> getKeywordNamesListFromCategoryKeywords(CategoryKeywords categoryKeywords) {

        List<String> keywordNames = new ArrayList<>();
        List<String> atmosphere = categoryKeywords.getAtmosphere();
        List<String> facility = categoryKeywords.getFacility();
        List<String> purpose = categoryKeywords.getPurpose();
        List<String> theme = categoryKeywords.getTheme();
        List<String> menu = categoryKeywords.getMenu();
        if (atmosphere != null) {
            keywordNames.addAll(atmosphere);
        }
        if (facility != null) {
            keywordNames.addAll(facility);
        }
        if (purpose != null) {
            keywordNames.addAll(purpose);
        }
        if (theme != null) {
            keywordNames.addAll(theme);
        }
        if (menu != null) {
            keywordNames.addAll(menu);
        }

        return keywordNames;
    }

    @LogTrace
    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    @LogTrace
    public List<Cafe> findAllByOrderByStarRatingDescNameAsc() {
        return cafeRepository.findAllByOrderByStarRatingDescNameAsc();
    }

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
}
