package com.sideproject.cafe_cok.member.domain.repository;


import com.sideproject.cafe_cok.constant.TestConstants;
import com.sideproject.cafe_cok.member.domain.Feedback;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    @DisplayName("feedbackCategory 기반으로 id 순으로 내림차순 정렬된 feedback 리스트를 조회한다.")
    void find_by_category_order_by_id_desc() {

        //given
        Feedback feedback1 = new Feedback(FEEDBACK_EMAIL, FeedbackCategory.IMPROVEMENT_SUGGESTION, FEEDBACK_CONTENT);
        Feedback feedback2 = new Feedback(FEEDBACK_EMAIL_2, FeedbackCategory.IMPROVEMENT_SUGGESTION, FEEDBACK_CONTENT_2);
        Feedback saveFeedback1 = feedbackRepository.save(feedback1);
        Feedback saveFeedback2 = feedbackRepository.save(feedback2);

        //when
        List<Feedback> findFeedbacks = feedbackRepository.findByCategoryOrderByIdDesc(FeedbackCategory.IMPROVEMENT_SUGGESTION);

        //then
        assertThat(findFeedbacks).hasSize(2);
        assertThat(findFeedbacks).isSortedAccordingTo((target1, target2) -> Long.compare(target2.getId(), target1.getId()));
        assertThat(findFeedbacks).extracting("email").containsExactlyInAnyOrder(FEEDBACK_EMAIL, FEEDBACK_EMAIL_2);
        assertThat(findFeedbacks).extracting("content")
                .containsExactlyInAnyOrder(FEEDBACK_CONTENT, FEEDBACK_CONTENT_2);

    }
}