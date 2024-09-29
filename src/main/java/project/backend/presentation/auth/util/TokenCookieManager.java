package project.backend.presentation.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenCookieManager {

  private static final int DEFAULT_EXPIRATION = 24 * 60 * 60;  // 1일 기본 만료 시간
  private static final String COOKIE_PATH = "/";

  public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    addRefreshTokenCookie(response, refreshToken, DEFAULT_EXPIRATION);
  }

  public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, int maxAge) {
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);      // 클라이언트에서 접근 불가
    refreshTokenCookie.setSecure(true);        // HTTPS 환경에서만 전송
    refreshTokenCookie.setPath(COOKIE_PATH);   // 경로 설정
    refreshTokenCookie.setMaxAge(maxAge);      // 만료 시간 설정
    response.addCookie(refreshTokenCookie);
  }

  public void removeRefreshTokenCookie(HttpServletResponse response) {
    Cookie refreshTokenCookie = new Cookie("refreshToken", null);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setPath(COOKIE_PATH);
    refreshTokenCookie.setMaxAge(0);  // 즉시 만료
    response.addCookie(refreshTokenCookie);
  }
}
