package project.backend.presentation.auth.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class TokenCookieManager {

  private static final int DEFAULT_EXPIRATION = 24 * 60 * 60;  // 1일 기본 만료 시간
  private static final String COOKIE_PATH = "/";

  @Value("${jwt.refresh_cookie_name}")
  private String refreshTokenCookieName;

  public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    addRefreshTokenCookie(response, refreshToken, DEFAULT_EXPIRATION);
  }

  public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, int maxAge) {
    ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, refreshToken)
                                          .path(COOKIE_PATH)
                                          .maxAge(maxAge)
                                          .httpOnly(true)
                                          .secure(true)
                                          .sameSite("None")
                                          .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  public void removeRefreshTokenCookie(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, "")
                                          .path(COOKIE_PATH)
                                          .maxAge(0)  // 즉시 만료
                                          .httpOnly(true)
                                          .secure(true)
                                          .sameSite("None")
                                          .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }
}
