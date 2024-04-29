package com.sideproject.hororok.utils;

import java.util.regex.Pattern;

public class Constants {

    public static final String IMAGE_URL_PREFIX = "https:";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    public static final String MEMBER_IMAGE_DIR = "member";
    public static final String REVIEW_IMAGE_DIR = "review";

    public static final String DECODED_URL_SPLIT_STR = "com/";
    public static final String URL_DECODER_DECODE_ENC = "UTF-8";
    


}
