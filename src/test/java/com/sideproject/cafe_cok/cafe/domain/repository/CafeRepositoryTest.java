package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.cafe.exception.NoSuchCafeException;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.PlanCafe;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CafeRepositoryTest {

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private OperationHourRepository operationHourRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 카페ID로_카페를_조회한다() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();
        Cafe savedCafe = cafeRepository.save(cafe);

        //when
        Cafe findCafe = cafeRepository.getById(savedCafe.getId());

        //then
        assertThat(findCafe).isEqualTo(savedCafe);
        assertThat(findCafe.getName()).isEqualTo(CAFE_NAME);
        assertThat(findCafe.getPhoneNumber()).isEqualTo(CAFE_PHONE_NUMBER);
        assertThat(findCafe.getRoadAddress()).isEqualTo(CAFE_ROAD_ADDRESS);
        assertThat(findCafe.getLongitude()).isEqualTo(CAFE_LONGITUDE);
        assertThat(findCafe.getLatitude()).isEqualTo(CAFE_LATITUDE);
    }

    @Test
    @DisplayName("존재하지 않는 cafeId로 조회 시 에러를 반환한다.")
    void 카페ID로_조회_시_존재하지_않음녀_에러를_반환한다() {

        //when & then
        assertThatExceptionOfType(NoSuchCafeException.class)
                .isThrownBy(() -> cafeRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 카페가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("전체 카페를 id 기반 내림차순으로 조회한다.")
    void 전체_카페를_카페_ID_기반_내림차순으로_조회한다() {

        //given
        Cafe cafe1 = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();
        Cafe savedCafe1 = cafeRepository.save(cafe1);

        Cafe cafe2 = Cafe.builder()
                .name(CAFE_NAME_2)
                .phoneNumber(CAFE_PHONE_NUMBER_2)
                .roadAddress(CAFE_ROAD_ADDRESS_2)
                .longitude(CAFE_LONGITUDE_2)
                .latitude(CAFE_LATITUDE_2)
                .kakaoId(CAFE_KAKAO_ID_2)
                .build();
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        //when
        List<Cafe> findCafes = cafeRepository.findAllByOrderByIdDesc();

        //then
        assertThat(findCafes).hasSize(2);
        assertThat(findCafes).isSortedAccordingTo((target1, target2) -> Long.compare(target2.getId(), target1.getId()));
        assertThat(findCafes).extracting("name").containsExactlyInAnyOrder(CAFE_NAME_2, CAFE_NAME);
        assertThat(findCafes).extracting("phoneNumber")
                .containsExactlyInAnyOrder(CAFE_PHONE_NUMBER_2, CAFE_PHONE_NUMBER);
        assertThat(findCafes).extracting("roadAddress")
                .containsExactlyInAnyOrder(CAFE_ROAD_ADDRESS, CAFE_ROAD_ADDRESS_2);
        assertThat(findCafes).extracting("longitude")
                .containsExactlyInAnyOrder(CAFE_LONGITUDE, CAFE_LONGITUDE_2);
        assertThat(findCafes).extracting("latitude")
                .containsExactlyInAnyOrder(CAFE_LATITUDE, CAFE_LATITUDE_2);
    }

    @Test
    @DisplayName("카카오 map API의 id 기반으로 해당하는 카페가 존재하는지 확인한다.")
    void 카카오_MAP_API의_장소_ID로_카페가_존재하는지_확인한다() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();
        Cafe savedCafe = cafeRepository.save(cafe);

        //when
        boolean isExist = cafeRepository.existsByKakaoId(CAFE_KAKAO_ID);

        //then
        assertThat(isExist).isTrue();
    }

    @Test
    void 시간_정보와_SORT로_카페_목록을_조회한다() {

        //given
        Cafe cafe1 = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .starRating(CAFE_STAR_RATING)
                .build();
        Cafe savedCafe1 = cafeRepository.save(cafe1);

        Cafe cafe2 = Cafe.builder()
                .name(CAFE_NAME_2)
                .phoneNumber(CAFE_PHONE_NUMBER_2)
                .roadAddress(CAFE_ROAD_ADDRESS_2)
                .longitude(CAFE_LONGITUDE_2)
                .latitude(CAFE_LATITUDE_2)
                .kakaoId(CAFE_KAKAO_ID_2)
                .starRating(CAFE_STAR_RATING_2)
                .build();
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        OperationHour operationHour1 = OperationHour.builder()
                .date(OPERATION_HOUR_DATE.getDayOfWeek())
                .openingTime(OPERATION_HOUR_START_TIME)
                .closingTime(OPERATION_HOUR_END_TIME)
                .isClosed(OPERATION_IS_CLOSED)
                .cafe(savedCafe1)
                .build();
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);

        OperationHour operationHour2 = OperationHour.builder()
                .date(OPERATION_HOUR_DATE.getDayOfWeek())
                .openingTime(OPERATION_HOUR_START_TIME)
                .closingTime(OPERATION_HOUR_END_TIME)
                .isClosed(OPERATION_IS_CLOSED)
                .cafe(savedCafe2)
                .build();
        OperationHour savedOperationHour2 = operationHourRepository.save(operationHour2);

        Sort sort = Sort.by(Sort.Order.desc("starRating"));

        //when
        List<Cafe> findCafes = cafeRepository.findByDateAndTime(CAFE_SEARCH_CONDITION, sort);

        //then
        assertThat(findCafes).hasSize(2);
        assertThat(findCafes).isSortedAccordingTo((target1, target2) -> target2.getStarRating().compareTo(target1.getStarRating()));
        assertThat(findCafes).extracting("name").containsExactlyInAnyOrder(CAFE_NAME_2, CAFE_NAME);
        assertThat(findCafes).extracting("phoneNumber")
                .containsExactlyInAnyOrder(CAFE_PHONE_NUMBER_2, CAFE_PHONE_NUMBER);
        assertThat(findCafes).extracting("roadAddress")
                .containsExactlyInAnyOrder(CAFE_ROAD_ADDRESS, CAFE_ROAD_ADDRESS_2);
        assertThat(findCafes).extracting("longitude")
                .containsExactlyInAnyOrder(CAFE_LONGITUDE, CAFE_LONGITUDE_2);
        assertThat(findCafes).extracting("latitude")
                .containsExactlyInAnyOrder(CAFE_LATITUDE, CAFE_LATITUDE_2);

    }

    @Test
    void 시간_정보와_도보시간으로_범위내의_카페를_조회한다() {

        //given
        Cafe cafe1 = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .starRating(CAFE_STAR_RATING)
                .build();
        Cafe savedCafe1 = cafeRepository.save(cafe1);

        Cafe cafe2 = Cafe.builder()
                .name(CAFE_NAME_2)
                .phoneNumber(CAFE_PHONE_NUMBER_2)
                .roadAddress(CAFE_ROAD_ADDRESS_2)
                .longitude(CAFE_LONGITUDE_2)
                .latitude(CAFE_LATITUDE_2)
                .kakaoId(CAFE_KAKAO_ID_2)
                .starRating(CAFE_STAR_RATING_2)
                .build();
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        OperationHour operationHour1 = OperationHour.builder()
                .date(OPERATION_HOUR_DATE.getDayOfWeek())
                .openingTime(OPERATION_HOUR_START_TIME)
                .closingTime(OPERATION_HOUR_END_TIME)
                .isClosed(OPERATION_IS_CLOSED)
                .cafe(savedCafe1)
                .build();
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);

        OperationHour operationHour2 = OperationHour.builder()
                .date(OPERATION_HOUR_DATE.getDayOfWeek())
                .openingTime(OPERATION_HOUR_START_TIME)
                .closingTime(OPERATION_HOUR_END_TIME)
                .isClosed(OPERATION_IS_CLOSED)
                .cafe(savedCafe2)
                .build();
        OperationHour savedOperationHour2 = operationHourRepository.save(operationHour2);

        //when
        List<Cafe> findCafes = cafeRepository.findByDateAndTimeAndMinutes(CAFE_SEARCH_CONDITION);

        //then
        assertThat(findCafes).hasSize(1);
        assertThat(findCafes).extracting("name").containsExactlyInAnyOrder(CAFE_NAME);
        assertThat(findCafes).extracting("phoneNumber").containsExactlyInAnyOrder(CAFE_PHONE_NUMBER);
        assertThat(findCafes).extracting("roadAddress").containsExactlyInAnyOrder(CAFE_ROAD_ADDRESS);
        assertThat(findCafes).extracting("longitude").containsExactlyInAnyOrder(CAFE_LONGITUDE);
        assertThat(findCafes).extracting("latitude").containsExactlyInAnyOrder(CAFE_LATITUDE);
    }

    @Test
    void 계획ID와_일치_유형으로_카페의_목록을_조회한다() {

        //given
        Cafe cafe1 = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();
        Cafe savedCafe1 = cafeRepository.save(cafe1);

        Cafe cafe2 = Cafe.builder()
                .name(CAFE_NAME_2)
                .phoneNumber(CAFE_PHONE_NUMBER_2)
                .roadAddress(CAFE_ROAD_ADDRESS_2)
                .longitude(CAFE_LONGITUDE_2)
                .latitude(CAFE_LATITUDE_2)
                .kakaoId(CAFE_KAKAO_ID_2)
                .build();
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .build();
        Member savedMember = memberRepository.save(member);

        Plan plan = new Plan(savedMember, PLAN_LOCATION_NAME, PLAN_VISIT_DATE, PLAN_VISIT_START_TIME,
                PLAN_VISIT_END_TIME, PLAN_MINUTES, PLAN_MATCH_TYPE, PLAN_IS_SAVED_TRUE, PLAN_IS_SHARED_TRUE);
        Plan savedPlan = planRepository.save(plan);

        PlanCafe savedPlanCafe1 = new PlanCafe(savedPlan, savedCafe1, PLAN_MATCH_TYPE);
        PlanCafe savedPlanCafe2 = new PlanCafe(savedPlan, savedCafe2, PLAN_MATCH_TYPE);

        //when
        List<Cafe> findCafes = cafeRepository.findByPlanIdAndMatchType(savedPlan.getId(), PLAN_MATCH_TYPE);

        //then
        assertThat(findCafes).hasSize(2);
        assertThat(findCafes).extracting("name").containsExactlyInAnyOrder(CAFE_NAME_2, CAFE_NAME);
        assertThat(findCafes).extracting("phoneNumber")
                .containsExactlyInAnyOrder(CAFE_PHONE_NUMBER_2, CAFE_PHONE_NUMBER);
        assertThat(findCafes).extracting("roadAddress")
                .containsExactlyInAnyOrder(CAFE_ROAD_ADDRESS, CAFE_ROAD_ADDRESS_2);
        assertThat(findCafes).extracting("longitude")
                .containsExactlyInAnyOrder(CAFE_LONGITUDE, CAFE_LONGITUDE_2);
        assertThat(findCafes).extracting("latitude")
                .containsExactlyInAnyOrder(CAFE_LATITUDE, CAFE_LATITUDE_2);

    }

    @Test
    @DisplayName("latitude, longitude 기반으로 근처 카페를 조회한다.")
    void find_nearest_cafe_list_by_latitude_longitude() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();
        Cafe savedCafe = cafeRepository.save(cafe);

        //when
        List<Cafe> findCafes = cafeRepository.findNearestCafes(CAFE_LATITUDE, CAFE_LONGITUDE);

        //then
        assertThat(findCafes).hasSize(1);
        assertThat(findCafes).extracting("name").containsExactlyInAnyOrder(CAFE_NAME);
        assertThat(findCafes).extracting("phoneNumber").containsExactlyInAnyOrder(CAFE_PHONE_NUMBER);
        assertThat(findCafes).extracting("roadAddress").containsExactlyInAnyOrder(CAFE_ROAD_ADDRESS);
        assertThat(findCafes).extracting("longitude").containsExactlyInAnyOrder(CAFE_LONGITUDE);
        assertThat(findCafes).extracting("latitude").containsExactlyInAnyOrder(CAFE_LATITUDE);
    }

    @Test
    void 날짜와_정렬정보_제한된_개수로_카페를_조회한다() {

        //given
        Cafe cafe1 = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .starRating(CAFE_STAR_RATING)
                .build();
        Cafe savedCafe1 = cafeRepository.save(cafe1);

        Cafe cafe2 = Cafe.builder()
                .name(CAFE_NAME_2)
                .phoneNumber(CAFE_PHONE_NUMBER_2)
                .roadAddress(CAFE_ROAD_ADDRESS_2)
                .longitude(CAFE_LONGITUDE_2)
                .latitude(CAFE_LATITUDE_2)
                .kakaoId(CAFE_KAKAO_ID_2)
                .starRating(CAFE_STAR_RATING_2)
                .build();
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        OperationHour operationHour1 = OperationHour.builder()
                .date(OPERATION_HOUR_DATE.getDayOfWeek())
                .openingTime(OPERATION_HOUR_START_TIME)
                .closingTime(OPERATION_HOUR_END_TIME)
                .isClosed(OPERATION_IS_CLOSED)
                .cafe(savedCafe1)
                .build();
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);

        OperationHour operationHour2 = OperationHour.builder()
                .date(OPERATION_HOUR_DATE.getDayOfWeek())
                .openingTime(OPERATION_HOUR_START_TIME)
                .closingTime(OPERATION_HOUR_END_TIME)
                .isClosed(OPERATION_IS_CLOSED)
                .cafe(savedCafe2)
                .build();
        OperationHour savedOperationHour2 = operationHourRepository.save(operationHour2);
        Sort sort = Sort.by(Sort.Order.desc("starRating"));

        //when
        List<Cafe> findCafes = cafeRepository.findByDate(OPERATION_HOUR_DATE, sort, 1);

        //then
        assertThat(findCafes).hasSize(1);
        assertThat(findCafes).extracting("name").containsExactlyInAnyOrder(CAFE_NAME_2);
        assertThat(findCafes).extracting("phoneNumber").containsExactlyInAnyOrder(CAFE_PHONE_NUMBER_2);
        assertThat(findCafes).extracting("roadAddress").containsExactlyInAnyOrder(CAFE_ROAD_ADDRESS_2);
        assertThat(findCafes).extracting("longitude").containsExactlyInAnyOrder(CAFE_LONGITUDE_2);
        assertThat(findCafes).extracting("latitude").containsExactlyInAnyOrder(CAFE_LATITUDE_2);
    }

}