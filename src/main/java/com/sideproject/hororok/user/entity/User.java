package com.sideproject.hororok.user.entity;

import com.sideproject.hororok.entity.BaseEntity;
import com.sideproject.hororok.review.Entity.Review;
import com.sideproject.hororok.user.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();



    @Builder
    public User(String nickname, String email, Role role) {
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public User update(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
