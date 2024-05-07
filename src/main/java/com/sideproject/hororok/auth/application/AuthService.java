package com.sideproject.hororok.auth.application;

import com.sideproject.hororok.auth.domain.AuthToken;
import com.sideproject.hororok.auth.domain.OAuthToken;
import com.sideproject.hororok.auth.domain.OAuthTokenRepository;
import com.sideproject.hororok.auth.domain.redis.AuthRefreshToken;
import com.sideproject.hororok.auth.domain.redis.AuthRefreshTokenRepository;
import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.dto.OAuthMember;
import com.sideproject.hororok.auth.dto.request.TokenRenewalRequest;
import com.sideproject.hororok.auth.dto.response.AccessAndRefreshTokenResponse;
import com.sideproject.hororok.auth.dto.response.AccessTokenResponse;
import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.bookmark.domain.BookmarkFolderRepository;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenCreator tokenCreator;
    private final MemberRepository memberRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;

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
