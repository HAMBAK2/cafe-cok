package com.sideproject.cafe_cok.cafe.domain;

import com.sideproject.cafe_cok.cafe.exception.InvalidCafeException;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Setter
@Table(name = "cafes")
@Entity
public class Cafe extends BaseEntity {

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

    @Column(name = "longitude", nullable = false,
            precision = 17, scale = 14)
    private BigDecimal longitude;

    @Column(name = "latitude", nullable = false,
            precision = 17, scale = 14)
    private BigDecimal latitude;

    @Column(name = "star_rating", precision = 2, scale = 1)
    private BigDecimal starRating;

    @Column(name = "review_count")
    private Long reviewCount;

    @Column(name = "main_image")
    private String mainImage;

    protected Cafe(){}

    public Cafe(final String name, final String phoneNumber, final String roadAddress,
                final BigDecimal longitude, final BigDecimal latitude) {
        validatePhoneNumber(phoneNumber);

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.starRating = BigDecimal.ZERO;
        this.reviewCount = 0L;
    }

    public Cafe(final String name, final String phoneNumber, final String roadAddress,
                final BigDecimal longitude, final BigDecimal latitude, final String mainImage) {
        validatePhoneNumber(phoneNumber);

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.starRating = BigDecimal.ZERO;
        this.reviewCount = 0L;
        this.mainImage = mainImage;
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isEmpty()) return;
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if(!matcher.matches()) {
            throw new InvalidCafeException("전화번호 형식이 올바르지 않습니다.");
        }
    }


    public void addReviewCountAndCalculateStarRating(Integer starRating) {
        BigDecimal totalScore = this.starRating.multiply(BigDecimal.valueOf(this.reviewCount));
        BigDecimal newReviewScore = BigDecimal.valueOf(starRating);
        totalScore = totalScore.add(newReviewScore);

        this.reviewCount++;
        BigDecimal newReviewCount = BigDecimal.valueOf(this.reviewCount);
        this.starRating = totalScore.divide(newReviewCount, 2, RoundingMode.HALF_UP);
    }

}
