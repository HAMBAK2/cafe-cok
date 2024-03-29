package com.sideproject.hororok.auth.dto;


import com.sideproject.hororok.user.Role;
import com.sideproject.hororok.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;
    private String nickname;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email, String nickname) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.nickname = nickname;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("nickname"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .role(Role.GUEST)
                .build();
    }
}
