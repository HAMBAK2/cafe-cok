package com.sideproject.cafe_cok.member.domain.repository;

import com.sideproject.cafe_cok.member.domain.Feedback;
import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByCategoryOrderByIdDesc(final FeedbackCategory category);
}
