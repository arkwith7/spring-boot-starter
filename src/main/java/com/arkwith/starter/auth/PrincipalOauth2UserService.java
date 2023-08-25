package com.arkwith.starter.auth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.arkwith.starter.user.Member;
import com.arkwith.starter.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 소셜 로그인시 사용하는 사용자 정보 적재 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)  throws OAuth2AuthenticationException {
        log.info("getClientRegistration : " + userRequest.getClientRegistration());
        log.info("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); 
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : " + oAuth2User.getAttributes());

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Optional<Member> _member = userRepository.findByUsername(attributes.getUsername());
        Member member;

        if (_member.isEmpty()) {
            member = Member.builder()
                    .username(attributes.getUsername())
                    .nickname(attributes.getNickname())
                    .name(attributes.getName())
                    .picture(attributes.getPicture())
                    .email(attributes.getEmail())
                    .role(attributes.getRole())
                    .provider(attributes.getProvider())
                    .providerId(attributes.getProviderId())
                    .build();
            userRepository.save(member);
        } else {
            // member = _member.get();
            // Member update;
            Member existUser = _member.get(); 
            member = existUser.update(existUser.getUsername(),
                                   existUser.getNickname(),
                                   existUser.getName(),
                                   existUser.getPicture(),
                                   existUser.getEmail(),
                                   existUser.getProvider(),
                                   existUser.getProviderId(),
                                   existUser.getRole()
                                   );
            userRepository.save(member);
        }

        log.info("사용자ID가 {}인 사용자가 {}에 로그인 요청을 합니다.", 
                         member.getUsername(), 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) );

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
    
}
