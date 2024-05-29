package com.sideproject.cafe_cok.member.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationRepository;
import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.member.dto.response.*;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanSortException;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


import static org.springframework.data.domain.Sort.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final S3Uploader s3Uploader;

    private final CafeRepository cafeRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final KeywordRepository keywordRepository;
    private final ImageRepository imageRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CombinationRepository combinationRepository;

    private final Integer FIRST_PAGE_NUMBER = 0;
    private final Integer MAX_PAGE_SIZE = Integer.MAX_VALUE;

    public MyPageProfileResponse profile(final LoginMember loginMember) {

        Long memberId = loginMember.getId();
        Member findMember = memberRepository.getById(memberId);
        Long findReviewCount = reviewRepository.countReviewsByMemberId(memberId);

        return MyPageProfileResponse.of(findMember, findReviewCount);
    }

    /* TODO: 맴버의 프로필 이미지도 리사이즈 되어서 저장되어야 하는지 확인 후 수정 해야 함*/
    @Transactional
    public MyPageProfileEditResponse editProfile(final LoginMember loginMember,
                                                 final String nickname,
                                                 final MultipartFile file) {

        Member findMember = memberRepository.getById(loginMember.getId());
        if(nickname != null) findMember.changeNickname(nickname);
        if(file != null) {
            if(findMember.getPicture() != null) s3Uploader.delete(findMember.getPicture());
            String picture = s3Uploader.upload(file, Constants.MEMBER_ORIGIN_IMAGE_DIR);
            findMember.changePicture(picture);
        }

        return MyPageProfileEditResponse.from(findMember);
    }

    public MyPagePlansResponse getPlans(final LoginMember loginMember,
                                        final PlanSortBy planSortBy,
                                        final PlanStatus status,
                                        final Integer page,
                                        final Integer size) {

        Sort sortBy = getSortBy(planSortBy);
        PageRequest pageRequest = PageRequest.of(page-1, size, sortBy);
        List<PlanKeywordDto> plans = planRepository
                .findPlansByMemberIdAndCategory(loginMember.getId(), Category.PURPOSE, planSortBy, status, pageRequest);

        return new MyPagePlansResponse(page, plans);
    }

    public MyPagePlansAllResponse getPlansAll(final LoginMember loginMember,
                                              final PlanSortBy planSortBy,
                                              final PlanStatus status) {

        Sort sortBy = getSortBy(planSortBy);
        PageRequest pageRequest = PageRequest.of(FIRST_PAGE_NUMBER, MAX_PAGE_SIZE, sortBy);
        List<PlanKeywordDto> plans = planRepository
                .findPlansByMemberIdAndCategory(loginMember.getId(), Category.PURPOSE, planSortBy, status, pageRequest);

        return new MyPagePlansAllResponse(plans);
    }

    private Sort getSortBy(final PlanSortBy planSortBy) {
        Sort sortBy;
        switch (planSortBy){
            case RECENT -> sortBy = by(Direction.DESC, PlanSortBy.RECENT.getValue());
            case UPCOMING -> sortBy = by(Direction.ASC, PlanSortBy.UPCOMING.getValue(), "visitStartTime", "id");
            default -> throw new NoSuchPlanSortException();
        }

        return sortBy;
    }

    public MyPageCombinationResponse combination(final LoginMember loginMember) {

        List<CombinationDto> findCombinations = combinationRepository.findDtoByMemberId(loginMember.getId());
        if(findCombinations.isEmpty()) return new MyPageCombinationResponse();
        return new MyPageCombinationResponse(findCombinations);
    }

    public MyPagePlanDetailResponse planDetail(final LoginMember loginMember,
                                               final Long planId) {

        Plan findPlan = planRepository.getById(planId);
        List<Keyword> findKeywords = keywordRepository.findKeywordByPlanId(planId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);

        List<CafeDto> findSimilarCafes =
                getCafeDtoListByPlanIdAndMatchTypeAndImageType(loginMember.getId(), planId, PlanCafeMatchType.SIMILAR);
        List<CafeDto> findMatchCafes =
                getCafeDtoListByPlanIdAndMatchTypeAndImageType(loginMember.getId(), planId, PlanCafeMatchType.MATCH);

        return new MyPagePlanDetailResponse(findPlan, categoryKeywords, findSimilarCafes, findMatchCafes);
    }

    public List<CafeDto> getCafeDtoListByPlanIdAndMatchTypeAndImageType(final Long memberId,
                                                                        final Long planId,
                                                                        final PlanCafeMatchType matchType) {
        List<Cafe> findCafeList = cafeRepository.findByPlanIdAndMatchType(planId, matchType);
        List<CafeDto> cafeDtoList = findCafeList.stream()
                .map(cafe -> {
                    String findImageUrl =
                            imageRepository.getImageByCafeAndImageType(cafe, ImageType.CAFE_MAIN).getThumbnail();

                    List<BookmarkIdDto> findBookmarkIdDtoList = null;
                    if (memberId != null) findBookmarkIdDtoList =
                            bookmarkRepository.findBookmarkIdDtoListByCafeIdAndMemberId(cafe.getId(), memberId);
                    return new CafeDto(cafe, findImageUrl, findBookmarkIdDtoList);
                }).collect(Collectors.toList());
        return cafeDtoList;
    }
}
