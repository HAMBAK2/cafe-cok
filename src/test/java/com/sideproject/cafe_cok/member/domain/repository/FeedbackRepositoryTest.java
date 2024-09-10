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
    void 피드백_카테고리로_ID순_내림차순_정렬된_피드백_목록을_조회한다() {

        //given
        Feedback feedback1 = Feedback.builder()
                .email(FEEDBACK_EMAIL)
                .category(FeedbackCategory.IMPROVEMENT_SUGGESTION)
                .content(FEEDBACK_CONTENT)
                .build();
        Feedback saveFeedback1 = feedbackRepository.save(feedback1);

        Feedback feedback2 = Feedback.builder()
                .email(FEEDBACK_EMAIL_2)
                .category(FeedbackCategory.IMPROVEMENT_SUGGESTION)
                .content(FEEDBACK_CONTENT_2)
                .build();
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