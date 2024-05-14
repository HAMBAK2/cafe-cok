package com.sideproject.cafe_cok.admin.domain;

import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.cafe.exception.InvalidCafeException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Table(name = "cafes_copy")
@Entity
public class CafeCopy extends BaseEntity {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{2,5}-\\d{3,4}-\\d{4}$");

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "mapx", nullable = false)
    private Integer mapx;

    @Column(name = "mapy", nullable = false)
    private Integer mapy;

    @Column(name = "star_rating", precision = 2, scale = 1)
    private BigDecimal starRating;

    @Column(name = "review_count")
    private Long reviewCount;

    protected CafeCopy() {
    }

    public CafeCopy(final String name, final String phoneNumber, final String roadAddress,
                    final Integer mapx, final Integer mapy) {
        validatePhoneNumber(phoneNumber);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
        this.starRating = BigDecimal.ZERO;
        this.reviewCount = 0L;
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if(phoneNumber.isEmpty()) return;
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if(!matcher.matches()) {
            throw new InvalidCafeException("전화번호 형식이 올바르지 않습니다.");
        }
    }



}
