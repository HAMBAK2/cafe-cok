package com.sideproject.cafe_cok.member.domain;

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

    public Feedback(final String email,
                    final String content) {
        this.email = email;
        this.content = content;
    }
}
