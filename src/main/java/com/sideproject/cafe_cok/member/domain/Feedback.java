package com.sideproject.cafe_cok.member.domain;

import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String content;

    @Enumerated(value = EnumType.STRING)
    private FeedbackCategory category;

    @Builder
    public Feedback(final Long id,
                    final String email,
                    final FeedbackCategory category,
                    final String content) {
        this.id = id;
        this.email = email;
        this.category = category;
        this.content = content;
    }
}
