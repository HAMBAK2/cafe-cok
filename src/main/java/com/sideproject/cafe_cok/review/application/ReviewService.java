package com.sideproject.cafe_cok.review.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.global.error.exception.MissingRequiredValueException;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.keword.domain.CafeReviewKeyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.member.dto.response.MyPageReviewResponse;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.review.dto.MyPageReviewDto;
import com.sideproject.cafe_cok.review.dto.request.ReviewEditRequest;
import com.sideproject.cafe_cok.review.dto.response.ReviewCreateResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewDetailResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewEditResponse;
import com.sideproject.cafe_cok.utils.FormatConverter;
import com.sideproject.cafe_cok.utils.ListUtils;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final S3Uploader s3Uploader;

    private final CafeRepository cafeRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    private static final String KEYWORD = "keyword";

    @Transactional
    public ReviewCreateResponse createReview(final ReviewCreateRequest request,
                                             final LoginMember loginMember,
                                             final List<MultipartFile> files) {

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        findCafe.addReviewCountAndCalculateStarRating(request.getStarRating());
        Member findMember = memberRepository.getById(loginMember.getId());

        Review review = new Review(request, findCafe, findMember);
        Review savedReview = reviewRepository.save(review);

        if(request.getKeywords() == null) throw new MissingRequiredValueException(KEYWORD);
        else saveByReviewAndKeywordNames(savedReview, request.getKeywords());

        saveByReviewAndMultipartFiles(savedReview, files);
        return new ReviewCreateResponse(savedReview.getId());
    }

    @Transactional
    public ReviewDeleteResponse delete(final Long reviewId) {

        reviewRepository.deleteById(reviewId);
        return new ReviewDeleteResponse(reviewId);
    }

    public ReviewDetailResponse detail(final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);
        List<Image> findReviewImages = imageRepository.findByReviewIdAndImageType(reviewId, ImageType.REVIEW);
        CategoryKeywordsDto CategoryKeywords = new CategoryKeywordsDto(keywordRepository.findByReviewId(reviewId));
        return ReviewDetailResponse.of(findReview, findReviewImages, CategoryKeywords);
    }

    @Transactional
    public ReviewEditResponse edit(final ReviewEditRequest request,
                                   final List<MultipartFile> files,
                                   final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);
        if(request.getContent() != null) findReview.setContent(request.getContent());
        if(request.getSpecialNote() != null) findReview.setSpecialNote(request.getSpecialNote());
        if(request.getStarRating() != null) findReview.setStarRating(request.getStarRating());
        if(request.getDeletedImageIds() != null) deleteByIds(request.getDeletedImageIds());
        if(files != null) saveByReviewAndMultipartFiles(findReview, files);
        if(request.getKeywords() == null) throw new MissingRequiredValueException(KEYWORD);
        else changeByReviewAndKeywordNames(findReview, request.getKeywords());

        return new ReviewEditResponse(reviewId);
    }

    public MyPageReviewResponse getReviews(final LoginMember loginMember) {

        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        List<MyPageReviewDto> findReviewDtoList = findReviews.stream().map(review -> {
            List<ImageUrlDto> findImageUrlDtoList
                    = imageRepository.findImageUrlDtoListByReviewIdAndImageType(review.getId(), ImageType.REVIEW);
            List<KeywordDto> findKeywords = keywordRepository.findByReviewIdAndCategory(review.getId(), Category.MENU);
            if(findKeywords.size() > RECOMMEND_MENU_MAX_CNT)
                findKeywords = findKeywords.subList(0, RECOMMEND_MENU_MAX_CNT);
            return new MyPageReviewDto(review, findImageUrlDtoList, findKeywords);
        }).collect(Collectors.toList());

        return new MyPageReviewResponse(findReviewDtoList);
    }

    private void saveByReviewAndKeywordNames(final Review review,
                                             final List<String> keywordNames) {

        List<Keyword> findKeywords = keywordRepository.findByNameIn(keywordNames);
        List<CafeReviewKeyword> cafeReviewKeywords = findKeywords.stream()
                .map(keyword -> new CafeReviewKeyword(review.getCafe(), review, keyword))
                .collect(Collectors.toList());
        cafeReviewKeywordRepository.saveAll(cafeReviewKeywords);
    }

    private void changeByReviewAndKeywordNames(final Review review,
                                               final List<String> keywordNames) {
        List<String> findKeywordNames = keywordRepository.findNamesByReviewId(review.getId());
        if(!ListUtils.areListEqual(findKeywordNames, keywordNames)) {
            cafeReviewKeywordRepository.deleteByReviewId(review.getId());
            saveByReviewAndKeywordNames(review, keywordNames);
        }
    }

    private void saveByReviewAndMultipartFiles(final Review review,
                                               final List<MultipartFile> files) {

        if(files == null) return;
        List<Image> reviewImages = saveImagesObjectStorage(review, files);
        imageRepository.saveAll(reviewImages);
    }

    @Transactional
    public void deleteByIds(final List<Long> ids) {

        List<Image> findImages = imageRepository.findImageByIdIn(ids);
        for (Image findImage : findImages) {
            s3Uploader.delete(findImage.getOrigin());
            s3Uploader.delete(findImage.getThumbnail());
        }
        imageRepository.deleteAllByIdIn(ids);
    }

    private List<Image> saveImagesObjectStorage(final Review review,
                                                final List<MultipartFile> files) {

        List<Image> reviewImages = new ArrayList<>();
        Cafe cafe = review.getCafe();
        for (MultipartFile file : files) {
            String imageUrl = s3Uploader.upload(file, REVIEW_ORIGIN_IMAGE_DIR);
            reviewImages.add(
                    new Image(ImageType.REVIEW,
                            imageUrl,
                            FormatConverter.changePath(imageUrl, REVIEW_ORIGIN_IMAGE_DIR, REVIEW_THUMBNAIL_IMAGE_DIR),
                            cafe,
                            review));
        }
        return reviewImages;
    }
}
