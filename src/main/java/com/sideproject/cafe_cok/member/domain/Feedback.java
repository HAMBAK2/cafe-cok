package com.sideproject.cafe_cok.member.domain;

import com.sideproject.cafe_cok.member.domain.enums.FeedbackCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Getter
@Table(name = "feedbacks")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String content;

    @Enumerated(value = EnumType.STRING)
    private FeedbackCategory category;

    public Feedback(final String email,
                    final FeedbackCategory category,
                    final String content) {
        this.email = email;
        this.category = category;
        this.content = content;
    }
}
