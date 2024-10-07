package project.backend.presentation.auth.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.backend.business.auth.request.KakaoLoginServiceRequest;

@Component
public class KakaoLoginRequestConverter {

  @Value("http://localhost:3000/oauth/kakao/callback")
  private String localRedirectUri;

  @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
  private String productionRedirectUri;

  public KakaoLoginServiceRequest convertKakaoLoginRequest(HttpServletRequest request, String code) {
    String redirectUri = productionRedirectUri;
    if (request.getServerName().equals("localhost")) {
      redirectUri = localRedirectUri;
    }
    return KakaoLoginServiceRequest.builder()
                                   .code(code)
                                   .redirectUri(redirectUri)
                                   .build();
  }
}
