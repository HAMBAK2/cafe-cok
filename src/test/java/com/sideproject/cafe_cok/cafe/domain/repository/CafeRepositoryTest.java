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
    @DisplayName("cafeId기반으로 카페를 조회한다.")
    void get_by_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
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
    void get_by_non_existent_id() {

        //when & then
        assertThatExceptionOfType(NoSuchCafeException.class)
                .isThrownBy(() -> cafeRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 카페가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("전체 카페를 id 기반 내림차순으로 조회한다.")
    void find_all_by_order_by_id_desc() {

        //given
        Cafe cafe1 = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe cafe2 = new Cafe(CAFE_NAME_2, CAFE_PHONE_NUMBER_2, CAFE_ROAD_ADDRESS_2, CAFE_LONGITUDE_2, CAFE_LATITUDE_2, CAFE_KAKAO_ID_2);
        Cafe savedCafe1 = cafeRepository.save(cafe1);
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
    void exists_by_cafe_id() {

        //given
        Cafe cafe =
                new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);

        //when
        boolean isExist = cafeRepository.existsByKakaoId(CAFE_KAKAO_ID);

        //then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("date, startTime, endTime, sort 기반으로 카페 리스트를 조회한다.")
    void find_by_date_time_sort() {

        //given
        Cafe cafe1 = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe cafe2 = new Cafe(CAFE_NAME_2, CAFE_PHONE_NUMBER_2, CAFE_ROAD_ADDRESS_2, CAFE_LONGITUDE_2, CAFE_LATITUDE_2, CAFE_KAKAO_ID_2);
        cafe1.changeStarRating(CAFE_STAR_RATING);
        cafe2.changeStarRating(CAFE_STAR_RATING_2);
        Cafe savedCafe1 = cafeRepository.save(cafe1);
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        OperationHour operationHour1 =
                new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(), OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe1);
        OperationHour operationHour2 =
                new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(), OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe2);
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);
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
    @DisplayName("date, startTime, endTime, minutes 기반으로 도보 직선거리 minutes 이내의 카페를 조회한다.")
    void find_by_date_time_minutes() {

        //given
        Cafe cafe1 = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe cafe2 = new Cafe(CAFE_NAME_2, CAFE_PHONE_NUMBER_2, CAFE_ROAD_ADDRESS_2, CAFE_LONGITUDE_OUT_OF_RANGE, CAFE_LATITUDE_OUT_OF_RANGE, CAFE_KAKAO_ID_2);
        cafe1.changeStarRating(CAFE_STAR_RATING);
        cafe2.changeStarRating(CAFE_STAR_RATING_2);
        Cafe savedCafe1 = cafeRepository.save(cafe1);
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        OperationHour operationHour1 =
                new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(), OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe1);
        OperationHour operationHour2 =
                new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(), OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe2);
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);
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
    @DisplayName("planId, matchType을 기반으로 카페의 리스트를 조회한다.")
    void find_by_plan_id_match_type() {

        //given
        Cafe cafe1 = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe cafe2 = new Cafe(CAFE_NAME_2, CAFE_PHONE_NUMBER_2, CAFE_ROAD_ADDRESS_2, CAFE_LONGITUDE_2, CAFE_LATITUDE_2, CAFE_KAKAO_ID_2);
        Cafe savedCafe1 = cafeRepository.save(cafe1);
        Cafe savedCafe2 = cafeRepository.save(cafe2);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Plan plan = new Plan(savedMember, PLAN_LOCATION_NAME, PLAN_VISIT_DATE, PLAN_VISIT_START_TIME,
                PLAN_VISIT_END_TIME, PLAN_MINUTES, PLAN_MATCH_TYPE, PLAN_IS_SAVED, PLAN_IS_SHARED);
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
        Cafe cafe1 = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe1 = cafeRepository.save(cafe1);

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
    @DisplayName("date, sort, limit 기반으로 카페를 조회하며")
    void find_by_date_sort_limit() {

        //given
        Cafe cafe1 = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe cafe2 = new Cafe(CAFE_NAME_2, CAFE_PHONE_NUMBER_2, CAFE_ROAD_ADDRESS_2, CAFE_LONGITUDE_2, CAFE_LATITUDE_2, CAFE_KAKAO_ID_2);
        cafe1.changeStarRating(CAFE_STAR_RATING);
        cafe2.changeStarRating(CAFE_STAR_RATING_2);
        Cafe savedCafe1 = cafeRepository.save(cafe1);
        Cafe savedCafe2 = cafeRepository.save(cafe2);

        OperationHour operationHour1 =
                new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(), OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe1);
        OperationHour operationHour2 =
                new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(), OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe2);
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);
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