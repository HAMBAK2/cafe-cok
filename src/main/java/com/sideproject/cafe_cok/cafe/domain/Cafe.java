package com.sideproject.cafe_cok.cafe.domain;

import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.cafe.exception.InvalidCafeException;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.keword.domain.CafeReviewKeyword;
import com.sideproject.cafe_cok.utils.FormatConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sideproject.cafe_cok.utils.FormatConverter.*;
import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Setter
@Table(name = "cafes")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends BaseEntity {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{2,5}-\\d{3,4}-\\d{4}$");
    private static final Integer X_NUM_DIGITS = 3;
    private static final Integer Y_NUM_DIGITS = 2;

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

    @Column(name = "longitude",
            precision = 17, scale = 14)
    private BigDecimal longitude;

    @Column(name = "latitude",
            precision = 17, scale = 14)
    private BigDecimal latitude;

    @Column(name = "star_rating", precision = 2, scale = 1)
    private BigDecimal starRating;

    @Column(name = "review_count")
    private Long reviewCount;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @OneToMany(mappedBy = "cafe")
    private List<OperationHour> operationHours = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<CafeReviewKeyword> cafeReviewKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Image> images = new ArrayList<>();

    public Cafe(final String name,
                final String phoneNumber,
                final String roadAddress,
                final BigDecimal longitude,
                final BigDecimal latitude,
                final Long kakaoId) {
        validatePhoneNumber(convertFormatPhoneNumber(phoneNumber));
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.starRating = BigDecimal.ZERO;
        this.reviewCount = 0L;
        this.kakaoId = kakaoId;
    }

    public Cafe(final AdminCafeSaveRequest request) {
        this(request.getName(),
                request.getPhone(),
                request.getAddress(),
                request.getLongitude(),
                request.getLatitude(),
                request.getKakaoId());
    }

    public void changeCafe(final String name,
                           final String phoneNumber,
                           final String roadAddress,
                           final BigDecimal longitude,
                           final BigDecimal latitude) {
        validatePhoneNumber(phoneNumber);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isEmpty()) return;
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if(!matcher.matches()) {
            throw new InvalidCafeException("전화번호 형식이 올바르지 않습니다.");
        }
    }

    public void addReviewCountAndCalculateStarRating(final Integer starRating) {
        BigDecimal totalScore = this.starRating.multiply(BigDecimal.valueOf(this.reviewCount));
        BigDecimal newReviewScore = BigDecimal.valueOf(starRating);
        totalScore = totalScore.add(newReviewScore);

        this.reviewCount++;
        BigDecimal newReviewCount = BigDecimal.valueOf(this.reviewCount);
        this.starRating = totalScore.divide(newReviewCount, 2, RoundingMode.HALF_UP);
    }

    public void minusReviewCountAndCalculateStarRating(final Integer starRating) {
        BigDecimal totalScore = this.starRating.multiply(BigDecimal.valueOf(this.reviewCount));
        BigDecimal minusReviewScore = BigDecimal.valueOf(starRating);
        totalScore = totalScore.subtract(minusReviewScore);

        this.reviewCount--;
        BigDecimal newReviewCount = BigDecimal.valueOf(this.reviewCount);
        this.starRating = totalScore.divide(newReviewCount, 2, RoundingMode.HALF_UP);
    }

    public void changeStarRating(final Integer starRating) {
        this.starRating = BigDecimal.valueOf(starRating);
    }
}
