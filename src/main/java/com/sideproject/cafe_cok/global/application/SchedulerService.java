package com.sideproject.cafe_cok.global.application;

import com.sideproject.cafe_cok.auth.domain.OAuthTokenRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final S3Uploader s3Uploader;

    //매일 새벽 4시 정각 동작
    //TODO: 스케쥴링 시간을 정하여 실행시키고 삭제가 제대로 이루어지는지 확인 필요
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void deleteMembersInactiveForThreeMonths() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
        List<Member> findMemberList = memberRepository.findMembersInactiveForThreeMonths(threeMonthsAgo);

        if(findMemberList.isEmpty()) return;

        for (Member member : findMemberList) {

            if(member.getPicture() != null) s3Uploader.delete(member.getPicture());
            List<Review> findReviewList = reviewRepository.findByMemberId(member.getId());

            for (Review review : findReviewList) {
                List<Image> findImageList = imageRepository.findByReviewIdAndImageType(review.getId(), ImageType.REVIEW);
                for (Image image : findImageList) {
                    s3Uploader.delete(image.getThumbnail());
                    s3Uploader.delete(image.getOrigin());
                }
            }
        }

        oAuthTokenRepository.deleteAllByMemberIn(findMemberList);
        memberRepository.deleteAll(findMemberList);
    }
}
