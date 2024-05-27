package com.sideproject.cafe_cok.auth.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.dto.OAuthMember;
import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationKeywordRepository;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationRepository;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.repository.PlanCafeRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.auth.domain.AuthToken;
import com.sideproject.cafe_cok.auth.domain.OAuthToken;
import com.sideproject.cafe_cok.auth.domain.OAuthTokenRepository;
import com.sideproject.cafe_cok.auth.domain.redis.AuthRefreshToken;
import com.sideproject.cafe_cok.auth.domain.redis.AuthRefreshTokenRepository;
import com.sideproject.cafe_cok.auth.dto.request.TokenRenewalRequest;
import com.sideproject.cafe_cok.auth.dto.response.AccessAndRefreshTokenResponse;
import com.sideproject.cafe_cok.auth.dto.response.AccessTokenResponse;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkFolderRepository;
import com.sideproject.cafe_cok.bookmark.domain.repository.BookmarkRepository;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.WithdrawnMember;
import com.sideproject.cafe_cok.member.domain.repository.WithdrawnMemberRepository;
import com.sideproject.cafe_cok.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenCreator tokenCreator;
    private final MemberRepository memberRepository;
    private final WithdrawnMemberRepository withdrawnMemberRepository;
    private final ReviewRepository reviewRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;
    private final PlanRepository planRepository;
    private final PlanCafeRepository planCafeRepository;
    private final PlanKeywordRepository planKeywordRepository;
    private final CombinationRepository combinationRepository;
    private final CombinationKeywordRepository combinationKeywordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final ImageRepository imageRepository;

    private final String BASIC_FOLDER_NAME = "기본 폴더";
    private final String BASIC_FOLDER_COLOR = "#FE8282";
    private final Boolean BASIC_FOLDER_VISIBLE = true;
    private final Boolean BASIC_FOLDER_DEFAULT = true;


    @Transactional
    public AccessAndRefreshTokenResponse generateAccessAndRefreshToken(final OAuthMember oAuthMember) {
        Member foundMember = findMember(oAuthMember);

        OAuthToken savedOAuthToken = saveOAuthToken(oAuthMember, foundMember);
        AuthToken authToken = tokenCreator.createAuthToken(savedOAuthToken.getMember().getId());
        return new AccessAndRefreshTokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
    }

    public AccessTokenResponse generateAccessToken(final TokenRenewalRequest tokenRenewalRequest) {
        String refreshToken = tokenRenewalRequest.getRefreshToken();
        AuthToken authToken = tokenCreator.renewAuthToken(refreshToken);
        return new AccessTokenResponse(authToken.getAccessToken());
    }

    public Long extractMemberId(final String accessToken) {
        Long memberId = tokenCreator.extractPayload(accessToken);
        Member findMember = memberRepository.getById(memberId);
        return findMember.getId();
    }

    @Transactional
    public void logout(final LoginMember loginMember) {

        OAuthToken findOAuthToken = oAuthTokenRepository.getByMemberId(loginMember.getId());
        oAuthTokenRepository.delete(findOAuthToken);

        AuthRefreshToken findAuthRefreshToken = authRefreshTokenRepository.getById(loginMember.getId());
        authRefreshTokenRepository.delete(findAuthRefreshToken);
    }

    @Transactional
    public void withdrawal(final LoginMember loginMember) {

        //북마크 관련 정보 삭제
        List<BookmarkFolder> findBookmarkFolders = bookmarkFolderRepository.findByMemberId(loginMember.getId());
        for (BookmarkFolder findBookmarkFolder : findBookmarkFolders) {
            bookmarkRepository.deleteByBookmarkFolderId(findBookmarkFolder.getId());
            bookmarkFolderRepository.deleteById(findBookmarkFolder.getId());
        }

        //조합관련 정보 삭제
        List<Combination> findCombinations = combinationRepository.findByMemberId(loginMember.getId());
        for (Combination findCombination : findCombinations) {
            combinationKeywordRepository.deleteByCombinationId(findCombination.getId());
            combinationRepository.deleteById(findCombination.getId());
        }

        //계획 관련 정보 삭제
        List<Plan> findPlans = planRepository.findByMemberId(loginMember.getId());
        for (Plan findPlan : findPlans) {
            planCafeRepository.deleteByPlanId(findPlan.getId());
            planKeywordRepository.deleteByPlanId(findPlan.getId());
            planRepository.deleteById(findPlan.getId());
        }

        //리뷰 관련 정보 삭제
        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        for (Review findReview : findReviews) {
            cafeReviewKeywordRepository.deleteByReviewId(findReview.getId());
            imageRepository.deleteByReviewId(findReview.getId());
            reviewRepository.deleteById(findReview.getId());
        }

        //로그아웃 처리 후 탈퇴한 사용자 정보 테이블에 정보 추가 및 기존 정보 삭제
        logout(loginMember);
        Member findMember = memberRepository.getById(loginMember.getId());
        WithdrawnMember withdrawnMember = new WithdrawnMember(findMember);
        withdrawnMemberRepository.save(withdrawnMember);
        memberRepository.deleteById(findMember.getId());
    }

    private Member findMember(final OAuthMember oAuthMember) {
        String email = oAuthMember.getEmail();
        if(memberRepository.existsByEmail(email)) {
            return memberRepository.getByEmail(email);
        }

        return saveMember(oAuthMember);
    }

    private Member saveMember(final OAuthMember oAuthMember) {
        Member savedMember = memberRepository.save(oAuthMember.toMember());
        bookmarkFolderRepository
                .save(new BookmarkFolder(
                        BASIC_FOLDER_NAME, BASIC_FOLDER_COLOR,
                        BASIC_FOLDER_VISIBLE, BASIC_FOLDER_DEFAULT, savedMember));
        return savedMember;
    }

    private OAuthToken saveOAuthToken(final OAuthMember oAuthMember, final Member member) {
        Long memberId = member.getId();
        if (oAuthTokenRepository.existsByMemberId(memberId)) {
            OAuthToken findOAuthToken = oAuthTokenRepository.getByMemberId(memberId);
            findOAuthToken.changeRefreshToken(oAuthMember.getRefreshToken());
            return oAuthTokenRepository.save(findOAuthToken);
        }

        return oAuthTokenRepository.save(new OAuthToken(member, oAuthMember.getRefreshToken()));
    }



}
