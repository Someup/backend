package project.backend.business.auth.service.oauth;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.backend.common.auth.oauth.KakaoUserDetails;
import project.backend.common.auth.oauth.KakaoUserInfo;
import project.backend.entity.user.User;
import project.backend.repository.user.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserDetailsService extends DefaultOAuth2UserService {

  private static final String DEFAULT_ROLE = "USER";

  private final UserRepository userRepository;

  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
    String email = kakaoUserInfo.getEmail();
    String name = kakaoUserInfo.getName();
    String profileImageUrl = kakaoUserInfo.getProfileImageUrl();

    User user = userRepository.findByEmail(email).orElseGet(
        () -> userRepository.save(User.createUser(email, name, profileImageUrl)));

    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(DEFAULT_ROLE);

    return new KakaoUserDetails(user.getId(), email, List.of(authority),
        oAuth2User.getAttributes());
  }
}
