package project.backend.presentation.auth.util;

import static project.backend.business.auth.implement.KakaoLoginManager.BEARER;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.backend.business.auth.request.TokenServiceRequest;

@Component
public class TokenExtractor {

  @Value("${jwt.access_header}")
  private String accessTokenHeader;
  
  @Value("${jwt.refresh_header}")
  private String refreshTokenHeader;

  public TokenServiceRequest extractTokenRequest(HttpServletRequest request) {
    return TokenServiceRequest.builder()
                              .accessToken(extractAccessToken(request).orElse(null))
                              .refreshToken(extractRefreshToken(request).orElse(null))
                              .build();
  }

  private Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessTokenHeader))
                   .filter(accessToken -> accessToken.startsWith(BEARER))
                   .map(accessToken -> accessToken.replace(BEARER, ""));
  }

  private Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshTokenHeader))
                   .filter(refreshToken -> refreshToken.startsWith(BEARER))
                   .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }
}
