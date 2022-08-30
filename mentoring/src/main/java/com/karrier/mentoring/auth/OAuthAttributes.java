package com.karrier.mentoring.auth;


import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // 해당 로그인인 서비스가 naver인지 google인지 구분하여, 알맞게 매핑
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId.equals("naver")) {
            return ofNaver(userNameAttributeName,attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response"); // 네이버에서 받은 데이터에서 프로필 정보가 담긴 response 값을 꺼낸다.
        return new OAuthAttributes(attributes, userNameAttributeName,
                (String) response.get("name"),
                (String) response.get("email"),
                (String) response.get("profile_image"));
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(attributes, userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture"));
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(name)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
