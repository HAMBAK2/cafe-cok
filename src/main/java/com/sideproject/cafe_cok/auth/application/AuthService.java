package com.sideproject.cafe_cok.auth.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.dto.OAuthMember;
import com.sideproject.cafe_cok.auth.exception.InvalidRestoreMemberException;
import com.sideproject.cafe_cok.member.domain.Feedback;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import com.sideproject.cafe_cok.member.domain.enums.SocialType;
import com.sideproject.cafe_cok.member.domain.repository.FeedbackRepository;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
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
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.nickname.application.NicknameService;
import com.sideproject.cafe_cok.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenCreator tokenCreator;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;

    private final NicknameService nicknameService;

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

        Optional<OAuthToken> findOAuthToken = oAuthTokenRepository.findByMemberId(loginMember.getId());
        if(findOAuthToken.isPresent()) oAuthTokenRepository.delete(findOAuthToken.get());

        Optional<AuthRefreshToken> findRefreshToken = authRefreshTokenRepository.findById(loginMember.getId());
        if(findRefreshToken.isPresent()) authRefreshTokenRepository.delete(findRefreshToken.get());
    }

    @Transactional
    public void withdrawal(final LoginMember loginMember,
                           final String reason) {

        Member findMember = memberRepository.getById(loginMember.getId());
        findMember.changeDeletedAt(LocalDateTime.now());

        Feedback newFeedback = new Feedback(
                findMember.getEmail(),
                FeedbackCategory.WITHDRAWAL_REASON,
                reason);
        feedbackRepository.save(newFeedback);
        List<Review> findReviews = findMember.getReviews();
        findReviews.stream()
                .forEach(review -> review.getCafe().minusReviewCountAndCalculateStarRating(review.getStarRating()));
    }

    private Member findMember(final OAuthMember oAuthMember) {
        String email = oAuthMember.getEmail();
        if(memberRepository.existsByEmailAndDeletedAtIsNull(email)) {
            return memberRepository.getByEmailAndDeletedAtIsNull(email);
        }

        if(memberRepository.existsByEmailAndDeletedAtIsNotNull(email)) {
            Member foundMember = memberRepository.getByEmailAndDeletedAtIsNotNull(email);
            LocalDateTime deletedAt = foundMember.getDeletedAt();
            if(isMoreThanSevenDaysAgo(deletedAt)) throw new InvalidRestoreMemberException(deletedAt);
        }

        return saveMember(oAuthMember);
    }

    private boolean isMoreThanSevenDaysAgo(final LocalDateTime deletedAt) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deletedAtPlusSevenDay = deletedAt.plusDays(7);
        return now.isAfter(deletedAt) && now.isBefore(deletedAtPlusSevenDay);
    }

    private Member saveMember(final OAuthMember oAuthMember) {

        String randomNickname = nicknameService.generateNickname();
        Member targetMember = new Member(oAuthMember.getEmail(), randomNickname, SocialType.KAKAO);
        Member savedMember = memberRepository.save(targetMember);
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
