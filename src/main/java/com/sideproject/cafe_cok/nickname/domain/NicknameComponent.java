package com.sideproject.cafe_cok.nickname.domain;


import com.sideproject.cafe_cok.nickname.enums.NicknameComponentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@Table(name = "nickname_components")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameComponent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value", unique = true)
    private String value;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NicknameComponentType type;

    public NicknameComponent(final String value,
                             final NicknameComponentType type) {
        this.value = value;
        this.type = type;
    }
}
