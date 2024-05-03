package com.sideproject.hororok.review.domain.repository;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.common.annotation.RepositoryTest;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.exception.NoSuchReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;
import static org.assertj.core.api.Assertions.*;

class ReviewRepositoryTest extends RepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Test
    @DisplayName("리뷰 ID로 리뷰를 조회한다.")
    public void test_get_review_by_review_id() {

        Cafe cafe = 카페();
        Cafe savedCafe = cafeRepository.save(cafe);

        Member member = 사용자();
        Member savedMember = memberRepository.save(member);

        Review review = new Review("콘텐츠", "특이사항", 3, savedCafe, savedMember);
        Review savedReview = reviewRepository.save(review);

        Review findReview = reviewRepository.getById(savedReview.getId());

        assertThat(findReview.getId()).isEqualTo(savedReview.getId());
    }

    @Test
    @DisplayName("리뷰 ID에 해당하는 리뷰가 없는 경우 에러를 리턴한다.")
    public void test_return_error_not_exist_review_id() {

        Long notExistId = 3L;

        assertThatThrownBy(() -> reviewRepository.getById(notExistId))
                .isInstanceOf(NoSuchReviewException.class);
    }
}