package com.sideproject.hororok.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Constants {

    public static final String IMAGE_URL_PREFIX = "https:";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    public static final String MEMBER_IMAGE_DIR = "member";
    public static final String REVIEW_IMAGE_DIR = "review";

    public static final String DECODED_URL_SPLIT_STR = "com/";
    public static final String URL_DECODER_DECODE_ENC = "UTF-8";

    public static final Integer RECOMMEND_MENU_MAX_CNT = 3;

    public static final Integer CAFE_DETAIL_TOP_KEYWORD_MAX_CNT = 3;

    public static final Integer CAFE_DETAIL_IMAGE_MAX_CNT = 3;

    public static final Integer CAFE_DETAIL_BASIC_INFO_IMAGE_MAX_CNT = 6;

    public static final Integer USER_CHOICE_KEYWORD_CNT = 6;

    public static final Integer CAFE_DETAIL_BASIC_INFO_REVIEW_KEYWORD_CNT = 3;

    public static final Integer CAFE_DETAIL_BASIC_INFO_REVIEW_IMG_CNT = 5;

    public static final Integer CAFE_DETAIL_IMAGE_SIZE = 8;

    public static final Long NO_MEMBER_ID = 1L;

    public static final Double MAX_RADIUS = Double.valueOf(2000);
    public static final Integer MAX_RADIUS_TIME = 30;



}
