package project.backend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.backend.business.auth.implement.TokenProvider;
import project.backend.common.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  @Value("${jwt.access_header}")
  private String accessTokenHeader;
  private final String BEARER = "Bearer ";
  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      @Nullable HttpServletResponse response,
      @Nullable FilterChain filterChain) throws ServletException, IOException {
    log.info("HttpMethod = {}, URI = {}", request.getMethod(), request.getRequestURI());

    if (isRequestPassURI(request)) {
      Objects.requireNonNull(filterChain).doFilter(request, response);
      return;
    }

    String accessToken = extractAccessToken(request).orElse(null);

    // 비로그인 유저 예약 요청 처리
    if (isRequestSummaryURI(request) && accessToken == null) {
      Objects.requireNonNull(filterChain).doFilter(request, response);
      return;
    }

    // 토큰이 없거나 유효하지 않은 경우 401 반환
    if (accessToken == null || !tokenProvider.validate(accessToken)) {
      setErrorResponse(response);
      return;
    }

    // 토큰이 만료된 경우 401 반환 (또는 별도의 만료 처리)
    if (!tokenProvider.validateExpired(accessToken)) {
      setErrorResponse(response);
      return;
    }

    // 토큰이 유효한 경우 SecurityContext에 인증 정보 설정
    if (tokenProvider.validateExpired(accessToken) && tokenProvider.validate(accessToken)) {
      SecurityContextHolder.getContext()
                           .setAuthentication(tokenProvider.getAuthentication(accessToken));
    }

    Objects.requireNonNull(filterChain).doFilter(request, response);
  }

  private void setErrorResponse(HttpServletResponse response) throws IOException {
    Objects.requireNonNull(response).setStatus(ErrorCode.INVALID_ACCESS_TOKEN.getHttpStatus().value());
    response.setContentType("application/json; charset=UTF-8");

    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(Map.of(
        "status", ErrorCode.INVALID_ACCESS_TOKEN.getHttpStatus().value(),
        "error", ErrorCode.INVALID_ACCESS_TOKEN.getMessage()
    )));
  }

  private static boolean isRequestSummaryURI(HttpServletRequest request) {
    // 요약하기
    if (request.getRequestURI().equals("/post") && request.getMethod()
                                                          .equals(HttpMethod.POST.name())) {
      return true;
    }
    // 재요약하기
    if (request.getRequestURI().matches("/post/[0-9]+/summary") && request.getMethod().equals(
        HttpMethod.PATCH.name())) {
      return true;
    }
    return false;
  }

  private static boolean isRequestPassURI(HttpServletRequest request) {
    if (request.getRequestURI().equals("/")) {
      return true;
    }

    if (request.getRequestURI().startsWith("/auth")) {
      return true;
    }

    if (request.getRequestURI().startsWith("/exception")) {
      return true;
    }

    return false;
  }

  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessTokenHeader))
                   .filter(accessToken -> accessToken.startsWith(BEARER))
                   .map(accessToken -> accessToken.replace(BEARER, ""));
  }
}
