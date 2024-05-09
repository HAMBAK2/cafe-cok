package com.sideproject.cafe_cok.review.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.global.error.exception.MissingRequiredValueException;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageDto;
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
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.ListUtils;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.review.domain.*;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public ReviewCreateResponse createReview(
            final ReviewCreateRequest request, final LoginMember loginMember, final List<MultipartFile> files) {

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        findCafe.addReviewCountAndCalculateStarRating(request.getStarRating());
        Member findMember = memberRepository.getById(loginMember.getId());

        Review review =
                new Review(request.getContent(), request.getSpecialNote(), request.getStarRating(), findCafe, findMember);
        Review savedReview = reviewRepository.save(review);

        if(request.getKeywords() == null) {
            throw new MissingRequiredValueException(KEYWORD);
        } else saveByReviewAndKeywordNames(savedReview, request.getKeywords());
        if(files != null) saveByReviewAndMultipartFiles(savedReview, files);

        return new ReviewCreateResponse(savedReview.getId());
    }

    @Transactional
    public ReviewDeleteResponse delete(final Long reviewId) {

        imageRepository.deleteByReviewId(reviewId);
        cafeReviewKeywordRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
        return new ReviewDeleteResponse(reviewId);
    }

    public ReviewDetailResponse detail(final Long reviewId) {
        Review findReview = reviewRepository.getById(reviewId);
        List<ImageDto> reviewImages
                = ImageDto.fromList(imageRepository.findByReviewId(reviewId));
        CategoryKeywordsDto CategoryKeywords = new CategoryKeywordsDto(keywordRepository.findByReviewId(reviewId));

        return ReviewDetailResponse.of(findReview, reviewImages, CategoryKeywords);
    }

    public MyPageReviewResponse getMyPageReviews(final LoginMember loginMember) {

        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        List<MyPageReviewDto> findReviewDtos = findReviews.stream().map(review -> {
            List<ImageDto> findImages
                    = ImageDto.fromList(imageRepository.findByReviewId(review.getId()));
            List<KeywordDto> findKeywords
                    = KeywordDto.fromList(keywordRepository.findByReviewIdAndCategory(review.getId(), Category.MENU));

            if(findKeywords.size() > Constants.RECOMMEND_MENU_MAX_CNT)
                findKeywords = findKeywords.subList(0, Constants.RECOMMEND_MENU_MAX_CNT);

            return MyPageReviewDto.of(review, findImages, findKeywords);
        }).collect(Collectors.toList());

        return new MyPageReviewResponse(findReviewDtos);
    }

    private void saveByReviewAndKeywordNames(final Review review, final List<String> keywordNames) {

        for (String keywordName : keywordNames) {
            Keyword findKeyword = keywordRepository.getByName(keywordName);
            CafeReviewKeyword cafeReviewKeyword = new CafeReviewKeyword(review.getCafe(), review, findKeyword);
            cafeReviewKeywordRepository.save(cafeReviewKeyword);
        }
    }

    private void changeByReviewAndKeywordNames(final Review review, final List<String> keywordNames) {
        List<String> findKeywordNames = keywordRepository.findNamesByReviewId(review.getId());
        if(!ListUtils.areListEqual(findKeywordNames, keywordNames)) {
            cafeReviewKeywordRepository.deleteByReviewId(review.getId());
            saveByReviewAndKeywordNames(review, keywordNames);
        }
    }

    @Transactional
    public ReviewEditResponse edit(final ReviewEditRequest request, final List<MultipartFile> files, final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);

        if(request.getContent() != null) findReview.changeContent(request.getContent());
        if(request.getSpecialNote() != null) findReview.changeSpecialNote(request.getSpecialNote());
        if(request.getStarRating() != null) findReview.changeStarRating(request.getStarRating());
        if(request.getDeletedImageIds() != null) deleteByIds(request.getDeletedImageIds());
        if(files != null) saveByReviewAndMultipartFiles(findReview, files);
        if(request.getKeywords() == null) throw new MissingRequiredValueException(KEYWORD);
        else changeByReviewAndKeywordNames(findReview, request.getKeywords());

        return new ReviewEditResponse(reviewId);
    }

    private void saveByReviewAndMultipartFiles(final Review review, final List<MultipartFile> files) {

        List<Image> reviewImages = saveImagesObjectStorage(review, files);
        if(reviewImages.isEmpty()) return;
        for (Image reviewImage : reviewImages) imageRepository.save(reviewImage);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {

        List<String> findImageUrls = imageRepository.findImageUrlByIdIn(ids);
        for (String imageUrl : findImageUrls) s3Uploader.delete(imageUrl);
        imageRepository.deleteAllByIdIn(ids);
    }

    private List<Image> saveImagesObjectStorage(final Review review, final List<MultipartFile> files) {

        List<Image> reviewImages = new ArrayList<>();

        if(files.isEmpty()) return reviewImages;

        for (MultipartFile file : files) {
            Image reviewImage =
                    new Image(ImageType.REVIEW_IMAGE,
                            s3Uploader.upload(file, Constants.REVIEW_IMAGE_DIR).replace(Constants.IMAGE_URL_PREFIX, ""), review);
            reviewImages.add(reviewImage);
        }
        return reviewImages;
    }
}
