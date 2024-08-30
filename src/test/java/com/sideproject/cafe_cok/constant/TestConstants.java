package com.sideproject.cafe_cok.constant;

import com.sideproject.cafe_cok.cafe.condition.CafeSearchCondition;
import com.sideproject.cafe_cok.member.domain.enums.SocialType;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestConstants {

    /* MEMBER */
    public static final String MEMBER_EMAIL = "member@member.com";
    public static final String MEMBER_EMAIL_2 = "member2@member.com";
    public static final String MEMBER_NICKNAME = "nickname";
    public static final String MEMBER_NOT_EXIST_NICKNAME = "not exist nickname";
    public static final SocialType MEMBER_SOCIAL_TYPE = SocialType.KAKAO;

    /* COMBINATION */
    public static final String COMBINATION_NAME_1 = "combination_name_1";
    public static final String COMBINATION_NAME_2 = "combination_name_2";
    public static final String COMBINATION_ICON_1 = "combination_icon_1";
    public static final String COMBINATION_ICON_2 = "combination_icon_2";
    public static final Long NON_EXISTENT_ID = 999L;

    /* OPERATION HOUR */
    public static final LocalDate OPERATION_HOUR_DATE = LocalDate.of(2024, 10, 1);
    public static final LocalTime OPERATION_HOUR_START_TIME = LocalTime.of(10, 0);
    public static final LocalTime OPERATION_HOUR_END_TIME = LocalTime.of(12, 0);
    public static final Boolean OPERATION_IS_CLOSED = false;
    public static final LocalDate OPERATION_HOUR_DATE_2 = LocalDate.of(2024, 10, 2);
    public static final LocalTime OPERATION_HOUR_START_TIME_2 = LocalTime.of(20, 0);
    public static final LocalTime OPERATION_HOUR_END_TIME_2 = LocalTime.of(22, 0);
    public static final Boolean OPERATION_IS_CLOSED_2 = false;

    /* CAFE */
    public static final String CAFE_NAME = "cafe name";
    public static final String CAFE_PHONE_NUMBER = "010-000-0000";
    public static final String CAFE_ROAD_ADDRESS = "cafe road address";
    public static final BigDecimal CAFE_LONGITUDE = new BigDecimal("126.98055287409800");
    public static final BigDecimal CAFE_LATITUDE = new BigDecimal("37.57061772252790");
    public static final Integer CAFE_STAR_RATING = 1;
    public static final Long CAFE_KAKAO_ID = 1L;
    public static final String CAFE_NAME_2 = "cafe name2";
    public static final String CAFE_PHONE_NUMBER_2 = "010-000-0002";
    public static final String CAFE_ROAD_ADDRESS_2 = "cafe road address2";
    public static final BigDecimal CAFE_LONGITUDE_2 = new BigDecimal("126.98055287409800");
    public static final BigDecimal CAFE_LATITUDE_2 = new BigDecimal("37.57061772252790");
    public static final BigDecimal CAFE_LONGITUDE_OUT_OF_RANGE = new BigDecimal("126.90002764094500");
    public static final BigDecimal CAFE_LATITUDE_OUT_OF_RANGE = new BigDecimal("37.55172454590180");
    public static final Long CAFE_KAKAO_ID_2 = 2L;
    public static final Integer CAFE_STAR_RATING_2 = 2;
    public static final CafeSearchCondition CAFE_SEARCH_CONDITION =
            new CafeSearchCondition(OPERATION_HOUR_DATE, OPERATION_HOUR_START_TIME,
                    OPERATION_HOUR_END_TIME, CAFE_LATITUDE, CAFE_LONGITUDE, 30);


    /* BOOKMARK FOLDER */
    public static final String BOOKMARK_FOLDER_NAME_1 = "bookmark folder name1";
    public static final String BOOKMARK_FOLDER_COLOR_1 = "bookmark folder color1";
    public static final String BOOKMARK_FOLDER_NAME_2 = "bookmark folder name2";
    public static final String BOOKMARK_FOLDER_COLOR_2 = "bookmark folder color2";
    public static final Boolean BOOKMARK_IS_VISIBLE = true;
    public static final Boolean BOOKMARK_IS_DEFAULT_FOLDER = true;

    /* MENU */
    public static final String MENU_NAME_1 = "menu name1";
    public static final Integer MENU_PRICE_1 = 10000;
    public static final String MENU_NAME_2 = "menu name2";
    public static final Integer MENU_PRICE_2 = 20000;
    public static final String MENU_IMAGE_URL_DTO_PRICE_1 = "10,000원";
    public static final String MENU_IMAGE_URL_DTO_PRICE_2 = "20,000원";

    /* IMAGE */
    public static final String IMAGE_ORIGIN_URL_1 = "image origin url1";
    public static final String IMAGE_THUMBNAIL_URL_1 = "image thumbnail url1";
    public static final String IMAGE_ORIGIN_URL_2 = "image origin url2";
    public static final String IMAGE_THUMBNAIL_URL_2 = "image thumbnail url2";
    public static final Integer IMAGE_PAGE_CNT = 2;

    /* REVIEW */
    public static final String REVIEW_CONTENT = "review content";
    public static final String REVIEW_SPECIAL_NOTE = "review special note";
    public static final Integer REVIEW_STAR_RATING = 5;
    public static final String REVIEW_CONTENT_2 = "review content2";
    public static final String REVIEW_SPECIAL_NOTE_2 = "review special note2";
    public static final Integer REVIEW_STAR_RATING_2 = 4;
    public static final Integer REVIEW_PAGE_CNT = 2;

    /* PLAN */
    public static final String PLAN_LOCATION_NAME = "plan location name";
    public static final LocalDate PLAN_VISIT_DATE = LocalDate.of(2024, 10, 1);
    public static final LocalTime PLAN_VISIT_START_TIME = LocalTime.of(10, 0);
    public static final LocalTime PLAN_VISIT_END_TIME = LocalTime.of(12, 0);
    public static final Integer PLAN_MINUTES = 30;
    public static final MatchType PLAN_MATCH_TYPE = MatchType.MATCH;
    public static final Boolean PLAN_IS_SAVED = false;
    public static final Boolean PLAN_IS_SHARED = false;

    /* FEEDBACK */
    public static final String FEEDBACK_EMAIL = MEMBER_EMAIL;
    public static final String FEEDBACK_CONTENT = "feedback content";
    public static final String FEEDBACK_EMAIL_2 = MEMBER_EMAIL_2;
    public static final String FEEDBACK_CONTENT_2 = "feedback content2";

}
