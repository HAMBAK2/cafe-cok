package com.sideproject.hororok.cafe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.exception.InvalidCafeException;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.menu.domain.Menu;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.utils.calculator.GeometricUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    private BigDecimal longitude; //경도

    @Column(name = "latitude", nullable = false,
            precision = 17, scale = 14)
    private BigDecimal latitude; //위도

    @Column(name = "star_rating", precision = 2, scale = 1)
    private BigDecimal starRating;

    @Column(name = "review_count")
    private Long reviewCount;

    @OneToMany(mappedBy = "cafe")
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<CafeImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<OperationHour> operationHours = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<CafeReviewKeyword> cafeReviewKeywords = new ArrayList<>();

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

    private void validatePhoneNumber(final String phoneNumber) {
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
