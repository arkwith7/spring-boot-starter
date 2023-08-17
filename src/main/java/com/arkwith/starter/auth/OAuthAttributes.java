package com.arkwith.starter.auth;


import java.util.Map;

import com.arkwith.starter.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String nickname;
    private String name;
    private String picture;
    private String email;
    private Role role;
    private String provider;
    private String providerId;

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
    
        /* 구글인지 네이버인지 카카오인지 구분하기 위한 메소드 (ofGoogle, ofNaver, ofKaKao) */
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        else if("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);

        }

        return ofGoogle(userNameAttributeName, attributes);
            
    }

    // Google인 경우
    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .username((String) "google_" + attributes.get("sub"))
                .nickname((String) attributes.get("name"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .role(Role.SOCIAL)
                .provider("google")
                .providerId((String) attributes.get("sub"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    // NAVER인 경우
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        /* JSON형태이기 때문에 Map을 통해 데이터를 가져온다. */
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
         
        log.info("naver response : " + response);
         
        return OAuthAttributes.builder()
            .username((String) "naver_" + response.get("id"))
            .nickname((String) response.get("nickname"))
            .name((String) response.get("name"))
            .email((String) response.get("email"))
            .role(Role.SOCIAL)
            .provider("naver")
            .providerId((String) response.get("id"))
            .attributes(response)
            .nameAttributeKey(userNameAttributeName)
            .build();
        }
        
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
    
        return OAuthAttributes.builder()
            .username((String) "kakao_" + attributes.get("id").toString())
            .nickname((String) kakaoProfile.get("nickname"))
            .name((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .role(Role.SOCIAL)
            .provider("kakao")
            .providerId((String) attributes.get("id"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
        }
}
/*
 * 각 소셜 계정 가져오는 Data 포맷
<google>
{
   sub=103058387739722400130, 
   name=안창범, 
   given_name=창범, 
   family_name=안, 
   picture=https://lh3.googleusercontent.com/a/AEdFTp5SiCyTaOLog9sDPN6QhWwsUj7xPbfj4HQF0fdC=s96-c, email=chb20050@gmail.com, 
   email_verified=true, 
   locale=ko
}

<kakao>
{
    id=2632890179, 
    connected_at=2023-01-22T08:17:54Z, 
    properties = {nickname=안창범}, 
    kakao_account = {
        profile_nickname_needs_agreement=false, 
        profile={nickname=안창범}, 
        has_email=true, 
        email_needs_agreement=false, 
        is_email_valid=true, 
        is_email_verified=true, 
        email=chb2005@naver.com
    }
}

<naver>
{
    resultcode=00, 
    message=success, 
    response = {
        id=pvdq1FSG3VZlD7Cp3JuWfAFi-3xir6A-WPlP5f8kXIo, email=chb20050@gmail.com, 
        name=안창범
    }
}

<facebook>
{
    id=5483543425087412, 
    name=안창범, 
    email=chb2005@naver.com
}
 */