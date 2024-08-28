package com.sideproject.cafe_cok;

import com.sideproject.cafe_cok.member.domain.enums.SocialType;

import java.math.BigDecimal;

public class TestConstants {

    /* MEMBER */
    public static final String MEMBER_EMAIL = "test@test.com";
    public static final String MEMBER_NICKNAME = "nickname";
    public static final SocialType MEMBER_SOCIAL_TYPE = SocialType.KAKAO;

    /* COMBINATION */
    public static final String COMBINATION_NAME_1 = "combination_name_1";
    public static final String COMBINATION_NAME_2 = "combination_name_2";
    public static final String COMBINATION_ICON_1 = "combination_icon_1";
    public static final String COMBINATION_ICON_2 = "combination_icon_2";
    public static final Long NON_EXISTENT_ID = 999L;

    /* CAFE */
    public static final String CAFE_NAME = "cafe name";
    public static final String CAFE_PHONE_NUMBER = "010-000-0000";
    public static final String CAFE_ROAD_ADDRESS = "cafe road address";
    public static final BigDecimal CAFE_LONGITUDE = new BigDecimal("126.9780");
    public static final BigDecimal CAFE_LATITUDE = new BigDecimal("37.5665");

    /* BOOKMARK FOLDER */
    public static final String BOOKMARK_FOLDER_NAME_1 = "bookmark folder name1";
    public static final String BOOKMARK_FOLDER_COLOR_1 = "bookmark folder color1";
    public static final String BOOKMARK_FOLDER_NAME_2 = "bookmark folder name2";
    public static final String BOOKMARK_FOLDER_COLOR_2 = "bookmark folder color2";
    public static final Boolean BOOKMARK_IS_VISIBLE = true;
    public static final Boolean BOOKMARK_IS_DEFAULT_FOLDER = true;
    
}
