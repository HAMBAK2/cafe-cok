package com.sideproject.cafe_cok.cafe.application;

import com.sideproject.cafe_cok.admin.dto.AdminImageDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;
import com.sideproject.cafe_cok.cafe.dto.CafeOperationHourDto;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeUpdateRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuRequestDto;
import com.sideproject.cafe_cok.cafe.dto.response.CafeSaveResponse;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.cafe.domain.enums.OpenStatus;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.domain.repository.OperationHourRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.cafe.dto.response.*;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.image.dto.response.ImagesResponse;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import com.sideproject.cafe_cok.menu.dto.response.MenusResponse;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.dto.response.CafeReviewsResponse;
import com.sideproject.cafe_cok.util.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.util.Constants.*;
import static com.sideproject.cafe_cok.util.FormatConverter.*;
import static com.sideproject.cafe_cok.util.FormatConverter.getKoreanDayOfWeek;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;
    private final KeywordRepository keywordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final OperationHourRepository operationHourRepository;
    private final S3Uploader s3Uploader;

    public CafesResponse getNearestCafes(final BigDecimal latitude,
                                         final BigDecimal longitude,
                                         final Long memberId) {

        List<CafeDto> findCafes = findNearestCafes(latitude, longitude, memberId);
        return new CafesResponse(findCafes);
    }

    public CafesResponse findCafeByKeyword(final BigDecimal latitude,
                                           final BigDecimal longitude,
                                           final List<String> keywords,
                                           final Long memberId) {

        List<CafeDto> findCafes = findNearestCafes(latitude, longitude, memberId);
        List<CafeDto> filteredWithinRadiusCafes = new ArrayList<>();

        if(keywords != null && !keywords.isEmpty()) {
            for (CafeDto cafe : findCafes) {
                List<String> findNames = keywordRepository.findNamesByCafeId(cafe.getId());
                boolean allMatch = keywords.stream().allMatch(findNames::contains);
                if(allMatch) filteredWithinRadiusCafes.add(cafe);
            }
        }

        return new CafesResponse(filteredWithinRadiusCafes);
    }

    public CafeTopResponse detailTop(final Long cafeId,
                                     final Long memberId) {

        Cafe findCafe = cafeRepository.getById(cafeId);
        Image findImage = imageRepository.getImageByCafeAndImageType(findCafe, ImageType.CAFE_MAIN);
        List<KeywordDto> findKeywordDtoList =
                keywordRepository.findKeywordDtoListByCafeIdOrderByCountDesc(
                        cafeId, PageRequest.of(0, CAFE_DETAIL_TOP_KEYWORD_MAX_CNT));

        if(memberId != null) {
            List<BookmarkFolderIdsDto> findBookmarkFolderIdsDtoList
                    = bookmarkRepository.getBookmarkFolderIds(cafeId, memberId);
            return new CafeTopResponse(findCafe, findImage, findKeywordDtoList, findBookmarkFolderIdsDtoList);
        }
        return new CafeTopResponse(findCafe, findImage, findKeywordDtoList);
    }

    public CafeBasicResponse detailBasicInfo(final Long cafeId) {

        Cafe findCafe = cafeRepository.getById(cafeId);
        List<OperationHour> findOperationHours = operationHourRepository.findByCafeId(cafeId);
        List<String> businessHours = new ArrayList<>();
        List<String> closedDay = new ArrayList<>();
        OpenStatus openStatus = OpenStatus.CLOSE;
        for (OperationHour findOperationHour : findOperationHours) {
            if(findOperationHour.isClosed()) {
                closedDay.add(getKoreanDayOfWeek(findOperationHour.getDate()));
                continue;
            }
            if(findOperationHour.getDate().equals(LocalDate.now().getDayOfWeek())) {
                openStatus = checkOpenStatus(findOperationHour);
                businessHours.add(0, convertOperationHourToString(findOperationHour));
                continue;
            }
            businessHours.add(convertOperationHourToString(findOperationHour));
        }

        List<MenuImageUrlDto> findMenuImageUrlDtoList = menuRepository.getMenuImageUrls(cafeId);
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<ImageUrlDto> imageUrlDtoList = getImageUrlDtoListByCafeId(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtoList(cafeId, CAFE_DETAIL_BASIC_REVIEW_PAGE_CNT);

        return new CafeBasicResponse(
                findCafe, openStatus, businessHours, closedDay,
                findMenuImageUrlDtoList, imageUrlDtoList, userChoiceKeywords, reviews);
    }

    @Transactional
    public CafeSaveResponse save(final AdminCafeSaveRequest request) {

        Cafe newCafe = new Cafe(request);
        Cafe savedCafe = cafeRepository.save(newCafe);
        List<Image> savedImages = new ArrayList<>();

        File converted = convertBase64StringToFile(request.getMainImage());
        String originImageUrl = s3Uploader.upload(converted, CAFE_MAIN_ORIGIN_IMAGE_DIR);
        String thumbnailImageDir = changePath(originImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_THUMBNAIL_DIR);
        String midImageDir = changePath(originImageUrl, CAFE_MAIN_ORIGIN_IMAGE_DIR, CAFE_MAIN_MEDIUM_IMAGE_DIR);
        Image mainImage = new Image(ImageType.CAFE_MAIN, originImageUrl, thumbnailImageDir, midImageDir, savedCafe);
        savedImages.add(imageRepository.save(mainImage));

        for (String otherImage : request.getOtherImages()) {
            converted = convertBase64StringToFile(otherImage);
            originImageUrl = s3Uploader.upload(converted, CAFE_ORIGIN_IMAGE_DIR);
            thumbnailImageDir = changePath(originImageUrl, CAFE_ORIGIN_IMAGE_DIR, CAFE_THUMBNAIL_IMAGE_DIR);
            Image othreImage = new Image(ImageType.CAFE, originImageUrl, thumbnailImageDir, savedCafe);
            savedImages.add(imageRepository.save(othreImage));
        }

        List<AdminMenuRequestDto> menus = request.getMenus();
        for (AdminMenuRequestDto menu : menus) {
            Menu newMenu = new Menu(menu.getName(), menu.getPrice(), savedCafe);
            Menu savedMenu = menuRepository.save(newMenu);

            if(menu.getImage() != null && !menu.getImage().isEmpty()) {
                converted = convertBase64StringToFile(menu.getImage());
                originImageUrl = s3Uploader.upload(converted, MENU_ORIGIN_IMAGE_DIR);
                thumbnailImageDir = changePath(originImageUrl, MENU_ORIGIN_IMAGE_DIR, MENU_THUMBNAIL_IMAGE_DIR);
                Image menuImage = new Image(ImageType.MENU, originImageUrl, thumbnailImageDir, savedCafe, savedMenu);
                savedImages.add(imageRepository.save(menuImage));
            }
        }

        List<CafeOperationHourDto> hours = request.getHours();
        if(checkoutInputHours(hours)) saveOperationHours(hours, savedCafe);

        return new CafeSaveResponse("Update successful", "/admin/cafe/" + savedCafe.getId());
    }

    @Transactional
    public CafeSaveResponse update(final Long id,
                                   final AdminCafeUpdateRequest request) {

        Cafe findCafe = cafeRepository.getById(id);
        findCafe.setName(request.getName());
        findCafe.setRoadAddress(request.getAddress());
        findCafe.setPhoneNumber(request.getPhoneNumber());
        cafeRepository.save(findCafe);

        List<Image> savedImage = new ArrayList<>();

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
                savedImage.add(imageRepository.save(findImage));
            }
        }

        for (AdminImageDto otherImage : request.getOtherImages()) {

            File converted = convertBase64StringToFile(otherImage.getImageBase64());
            String originImageUrl = s3Uploader.upload(converted, CAFE_ORIGIN_IMAGE_DIR);
            String thumbnailImageUrl = changePath(originImageUrl, CAFE_ORIGIN_IMAGE_DIR, CAFE_THUMBNAIL_IMAGE_DIR);

            if(otherImage.getId() != null) {
                Optional<Image> optionalImage = imageRepository.findById(otherImage.getId());
                if(optionalImage.isPresent()) {
                    Image findImage = optionalImage.get();
                    s3Uploader.delete(findImage.getThumbnail());
                    s3Uploader.delete(findImage.getOrigin());
                    findImage.changeOrigin(originImageUrl);
                    findImage.changeThumbnail(thumbnailImageUrl);
                    savedImage.add(imageRepository.save(findImage));
                }
                continue;
            }

            Image newImage = new Image(ImageType.CAFE, originImageUrl, thumbnailImageUrl, findCafe);
            savedImage.add(imageRepository.save(newImage));
        }

        List<CafeOperationHourDto> hours = request.getHours();
        operationHourRepository.deleteByCafeId(id);
        if(checkoutInputHours(hours)) saveOperationHours(hours, findCafe);

        List<AdminMenuRequestDto> menus = request.getMenus();
        for (AdminMenuRequestDto menu : menus) {

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

                savedImage.add(imageRepository.save(targetImage));
            }
        }

        return new CafeSaveResponse("Update successful", "/admin/cafe/" + id);
    }

    public boolean isExistByKakaoId(final Long id) {
        return cafeRepository.existsByKakaoId(id);
    }

    public CafeReviewsResponse findReviews(final Long cafeId) {
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtoList(cafeId, Integer.MAX_VALUE);;
        return CafeReviewsResponse.of(userChoiceKeywords, reviews);
    }

    public ImagesResponse findImages(final Long cafeId) {

        List<ImageUrlDto> images = new ArrayList<>();
        List<ImageUrlDto> findCafeImageUrlDtoList = getCafeImageUrlDtoList(cafeId);
        List<ImageUrlDto> findReviewImageUrlDtoList =
                imageRepository.findImageUrlDtoListByCafeIdImageType(cafeId, ImageType.REVIEW);

        images.addAll(findCafeImageUrlDtoList);
        images.addAll(findReviewImageUrlDtoList);
        return ImagesResponse.from(images);
    }

    public MenusResponse findMenus(final Long cafeId) {

        List<MenuImageUrlDto> findMenus = menuRepository.getMenuImageUrls(cafeId);
        return new MenusResponse(findMenus);
    }

    private List<ImageUrlDto> getCafeImageUrlDtoList(final Long cafeId) {
        return imageRepository
                .findCafeImageUrlDtoListByCafeId(
                        cafeId,
                        PageRequest.of(0, CAFE_DETAIL_IMAGE_PAGE_CNT));
    }


    private void saveOperationHours(final List<CafeOperationHourDto> hours, final Cafe cafe) {
        List<OperationHour> newOperationHours = new ArrayList<>();
        for (CafeOperationHourDto hour : hours) {
            DayOfWeek day = getDyaOfWeekByKoreanDay(hour.getDay());
            LocalTime startTime = LocalTime.of(hour.getStartHour(), hour.getStartMinute());
            LocalTime endTime = LocalTime.of(hour.getEndHour(), hour.getEndMinute());
            boolean isClosed = false;

            if(startTime.equals(LocalTime.MIDNIGHT) && endTime.equals(LocalTime.MIDNIGHT)) isClosed = true;
            OperationHour newOperationHour = new OperationHour(day, startTime, endTime, isClosed, cafe);
            newOperationHours.add(newOperationHour);
        }

        operationHourRepository.saveAll(newOperationHours);
    }

    private boolean checkoutInputHours(List<CafeOperationHourDto> hours) {

        for (CafeOperationHourDto hour : hours) {
            if(hour.getStartHour() != 0) return true;
            if(hour.getStartMinute() != 0) return true;
            if(hour.getEndHour() != 0) return true;
            if(hour.getEndMinute() != 0) return true;
        }

        return false;
    }


    private List<ImageUrlDto> getImageUrlDtoListByCafeId(final Long cafeId) {

        List<ImageUrlDto> imageUrlDtoList = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, CAFE_DETAIL_IMAGE_PAGE_CNT);
        List<ImageUrlDto> findCafeImageUrlDtoList = imageRepository.findCafeImageUrlDtoListByCafeId(cafeId, pageable);

        pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_IMAGE_PAGE_CNT - findCafeImageUrlDtoList.size());
        List<ImageUrlDto> findReviewImageUrlDtoList =
                imageRepository.findImageUrlDtoListByCafeIdImageType(cafeId, ImageType.REVIEW, pageable);

        imageUrlDtoList.addAll(findCafeImageUrlDtoList);
        imageUrlDtoList.addAll(findReviewImageUrlDtoList);
        return imageUrlDtoList;
    }

    private OpenStatus checkOpenStatus(final OperationHour operationHour) {

        LocalTime openingTime = operationHour.getOpeningTime();
        LocalTime closingTime = operationHour.getClosingTime();

        LocalTime now = LocalTime.now();

        boolean before = openingTime.isBefore(now);
        boolean after = closingTime.isAfter(now);

        if(after && before) return OpenStatus.OPEN;
        return OpenStatus.CLOSE;
    }

    private List<KeywordCountDto> getUserChoiceKeywordCounts(final Long cafeId) {
        return keywordRepository.findKeywordCountDtoListByCafeIdOrderByCountDesc(
                cafeId, PageRequest.of(0, USER_CHOICE_KEYWORD_CNT));
    }

    private List<CafeDto> findNearestCafes(final BigDecimal latitude,
                                           final BigDecimal longitude,
                                           final Long memberId) {
        List<Cafe> findCafes = cafeRepository.findNearestCafes(latitude, longitude);
        List<CafeDto> cafeDtoList = findCafes.stream()
                .map(cafe -> {
                    String findImageUrl =
                            imageRepository.getImageByCafeAndImageType(cafe, ImageType.CAFE_MAIN).getThumbnail();

                    List<BookmarkFolderIdsDto> findBookmarkFolderIdsDtoList = null;
                    if(memberId != null) findBookmarkFolderIdsDtoList =
                            bookmarkRepository.getBookmarkFolderIds(cafe.getId(), memberId);
                    return new CafeDto(cafe, findImageUrl, findBookmarkFolderIdsDtoList);
                }).collect(Collectors.toList());
        return cafeDtoList;
    }

    private List<CafeDetailReviewDto> getCafeDetailReviewDtoList(final Long cafeId,
                                                                 final Integer reviewCnt) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(0, reviewCnt, sort);
        List<Review> reviews = reviewRepository.findByCafeId(cafeId, pageable);
        return convertReviewsToCafeDetailReviewDtoList(cafeId, reviews);
    }

    private List<CafeDetailReviewDto> convertReviewsToCafeDetailReviewDtoList(final Long cafeId,
                                                                              final List<Review> reviews) {
        List<CafeDetailReviewDto> cafeDetailReviewDtoList = new ArrayList<>();
        for (Review review : reviews) {

            Pageable pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_KEYWORD_CNT);
            List<String> recommendMenus = keywordRepository
                    .findNamesByReviewIdAndCategory(review.getId(), Category.MENU, pageable);

            Sort sort = Sort.by(Sort.Order.desc("id"));
            pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_IMG_CNT, sort);

            List<ImageUrlDto> findImageUrlDtoList = imageRepository
                    .findImageUrlDtoListByReviewId(review.getId(), pageable);

            cafeDetailReviewDtoList.add(new CafeDetailReviewDto(review, findImageUrlDtoList, recommendMenus));
        }
        return cafeDetailReviewDtoList;
    }

}
