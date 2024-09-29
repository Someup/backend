package project.backend.presentation.auth.util;

import static project.backend.business.auth.implement.KakaoLoginManager.BEARER;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.backend.business.auth.request.TokenServiceRequest;

@Slf4j
@Component
public class TokenExtractor {

  @Value("${jwt.access_header}")
  private String accessTokenHeader;

  @Value("${jwt.refresh_cookie_name}")
  private String refreshTokenCookieName;

  public TokenServiceRequest extractTokenRequest(HttpServletRequest request) {
    String accessToken = extractAccessToken(request).orElse(null);
    String refreshToken = extractRefreshToken(request).orElse(null);
    log.info("Token extraction initiated with accessToken: {}, refreshToken: {}", accessToken, refreshToken);
    return TokenServiceRequest.builder()
                              .accessToken(accessToken)
                              .refreshToken(refreshToken)
                              .build();
  }

  private Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessTokenHeader))
                   .filter(accessToken -> accessToken.startsWith(BEARER))
                   .map(accessToken -> accessToken.replace(BEARER, ""));
  }

  private Optional<String> extractRefreshToken(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return Optional.empty();
    }
    for (Cookie cookie : request.getCookies()) {
      if (refreshTokenCookieName.equals(cookie.getName())) {
        return Optional.ofNullable(cookie.getValue());
      }
    }
    return Optional.empty();
  }
}
