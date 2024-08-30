package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static com.sideproject.cafe_cok.constant.TestConstants.OPERATION_IS_CLOSED;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class OperationHourRepositoryTest {

    @Autowired
    private OperationHourRepository operationHourRepository;

    @Autowired
    private CafeRepository cafeRepository;


    @Test
    @DisplayName("cafeId 기반으로 operationHour 리스트를 조회한다.")
    void find_by_cafe_id() {


        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);

        OperationHour operationHour1 = new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(),
                OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe);
        OperationHour operationHour2 = new OperationHour(OPERATION_HOUR_DATE_2.getDayOfWeek(),
                OPERATION_HOUR_START_TIME_2, OPERATION_HOUR_END_TIME_2, OPERATION_IS_CLOSED_2, savedCafe);
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);
        OperationHour savedOperationHour2 = operationHourRepository.save(operationHour2);

        //when
        List<OperationHour> findOperationHours = operationHourRepository.findByCafeId(savedCafe.getId());

        //then
        assertThat(findOperationHours).hasSize(2);
        assertThat(findOperationHours).extracting("date")
                .containsExactlyInAnyOrder(OPERATION_HOUR_DATE.getDayOfWeek(), OPERATION_HOUR_DATE_2.getDayOfWeek());
        assertThat(findOperationHours).extracting("openingTime")
                .containsExactlyInAnyOrder(OPERATION_HOUR_START_TIME, OPERATION_HOUR_START_TIME_2);
        assertThat(findOperationHours).extracting("closingTime")
                .containsExactlyInAnyOrder(OPERATION_HOUR_END_TIME, OPERATION_HOUR_END_TIME_2);
    }

    @Test
    @DisplayName("cafeId 기반으로 operation hour를 삭제한다.")
    void delete_by_cafe_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);

        OperationHour operationHour1 = new OperationHour(OPERATION_HOUR_DATE.getDayOfWeek(),
                OPERATION_HOUR_START_TIME, OPERATION_HOUR_END_TIME, OPERATION_IS_CLOSED, savedCafe);
        OperationHour operationHour2 = new OperationHour(OPERATION_HOUR_DATE_2.getDayOfWeek(),
                OPERATION_HOUR_START_TIME_2, OPERATION_HOUR_END_TIME_2, OPERATION_IS_CLOSED_2, savedCafe);
        OperationHour savedOperationHour1 = operationHourRepository.save(operationHour1);
        OperationHour savedOperationHour2 = operationHourRepository.save(operationHour2);

        //when
        operationHourRepository.deleteByCafeId(savedCafe.getId());
        boolean isExist1 = operationHourRepository.existsById(savedOperationHour1.getId());
        boolean isExist2 = operationHourRepository.existsById(savedOperationHour2.getId());

        //then
        assertThat(isExist1).isFalse();
        assertThat(isExist2).isFalse();
    }

}