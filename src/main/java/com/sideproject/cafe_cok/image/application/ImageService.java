package com.sideproject.cafe_cok.image.application;

import com.sideproject.cafe_cok.image.dto.response.ImageAllResponse;
import com.sideproject.cafe_cok.image.dto.response.ImagePageResponse;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageUrlCursorDto;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    public static final Integer CAFE_DETAIL_IMAGE_SIZE = 8;
    private static final Boolean HAS_NEXT_PAGE = true;
    private static final Boolean NO_NEXT_PAGE = false;

    public ImagePageResponse findByCafeId(final Long cafeId,
                                          final Long cursor) {

        List<ImageUrlDto> images = new ArrayList<>();
        Integer cafeDetailImageSize = CAFE_DETAIL_IMAGE_SIZE;

        if(cursor == null) {
            List<ImageUrlDto> findCafeImageUrlDtoList = getCafeImageUrlDtoList(cafeId);
            cafeDetailImageSize -= findCafeImageUrlDtoList.size();
            images.addAll(findCafeImageUrlDtoList);
        }

        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<ImageUrlCursorDto> findReviewImageUrlCursorDtoList = imageRepository
                .findImageUrlCursorDtoListByCafeIdAndImageType(
                        cafeId,
                        cursor,
                        ImageType.REVIEW,
                        PageRequest.of(0, cafeDetailImageSize, sort));

        if(findReviewImageUrlCursorDtoList.size() < cafeDetailImageSize) {
            images.addAll(convertFromImageUrlCursorDtoToImageUrlDto(findReviewImageUrlCursorDtoList));
            return new ImagePageResponse(images, NO_NEXT_PAGE);
        }

        Long newCursor = findReviewImageUrlCursorDtoList.get(findReviewImageUrlCursorDtoList.size() - 1).getCursor();
        images.addAll(convertFromImageUrlCursorDtoToImageUrlDto(findReviewImageUrlCursorDtoList));
        return new ImagePageResponse(images, newCursor, HAS_NEXT_PAGE);
    }

    public ImageAllResponse findByCafeIdAll(final Long cafeId) {

        List<ImageUrlDto> images = new ArrayList<>();
        List<ImageUrlDto> findCafeImageUrlDtoList = getCafeImageUrlDtoList(cafeId);
        List<ImageUrlDto> findReviewImageUrlDtoList =
                imageRepository.findImageUrlDtoListByCafeIdImageType(cafeId, ImageType.REVIEW);

        images.addAll(findCafeImageUrlDtoList);
        images.addAll(findReviewImageUrlDtoList);
        return ImageAllResponse.from(images);
    }

    private List<ImageUrlDto> getCafeImageUrlDtoList(final Long cafeId) {
        return imageRepository
                .findCafeImageUrlDtoListByCafeId(
                        cafeId,
                        PageRequest.of(0, CAFE_DETAIL_IMAGE_PAGE_CNT));
    }

    private List<ImageUrlDto> convertFromImageUrlCursorDtoToImageUrlDto(final List<ImageUrlCursorDto> dtoList) {
        return dtoList.stream()
                .map(dto -> new ImageUrlDto(dto.getOriginUrl(), dto.getThumbnailUrl()))
                .collect(Collectors.toList());
    }
}
