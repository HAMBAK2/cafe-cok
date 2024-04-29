package com.sideproject.hororok.review.application;

import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.domain.ReviewImage;
import com.sideproject.hororok.review.domain.repository.ReviewImageRepository;
import com.sideproject.hororok.review.dto.ReviewImageDto;
import com.sideproject.hororok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sideproject.hororok.utils.Constants.*;
import static com.sideproject.hororok.utils.Constants.IMAGE_URL_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewImageService {

    private final S3Uploader s3Uploader;

    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public void saveByReviewAndMultipartFiles(final Review review, final List<MultipartFile> files) {

        List<ReviewImage> reviewImages = saveImagesObjectStorage(review, files);
        for (ReviewImage reviewImage : reviewImages) reviewImageRepository.save(reviewImage);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {

        List<String> findImageUrls = reviewImageRepository.findImageUrlByIdIn(ids);
        for (String imageUrl : findImageUrls) s3Uploader.delete(imageUrl);
        reviewImageRepository.deleteAllByIdIn(ids);
    }

    public List<ReviewImageDto> getReviewImageDtosByReviewId(final Long reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findByReviewId(reviewId);
        return reviewImages.stream().map(ReviewImageDto::from)
                .collect(Collectors.toList());
    }

    public List<String> getReviewImageUrlsByCafeId(Long cafeId) {
        return reviewImageRepository.findImageUrlByCafeId(cafeId);
    }

    private List<ReviewImage> saveImagesObjectStorage(final Review review, final List<MultipartFile> files) {

        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile file : files) {
            ReviewImage reviewImage =
                    new ReviewImage(s3Uploader.upload(file, REVIEW_IMAGE_DIR)
                            .replace(IMAGE_URL_PREFIX, ""), review);
            reviewImages.add(reviewImage);
        }
        return reviewImages;
    }
}
