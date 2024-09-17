package project.backend.common.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.backend.common.auth.token.TokenProvider;

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
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("HttpMethod = {}, URI = {}", request.getMethod(), request.getRequestURI());
        if (isRequestPassURI(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = extractAccessToken(request).orElse(null);

        // 비로그인 유저 예약 요청 처리
        if (isRequestSummaryURI(request) && accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!tokenProvider.validateExpired(accessToken) && tokenProvider.validate(accessToken)) {
            String redirectUrl =
                    "https://" + request.getServerName() + "/api/exception/access-token-expired";
            response.sendRedirect(redirectUrl);
            return;
        }

        if (tokenProvider.validateExpired(accessToken) && tokenProvider.validate(accessToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
        }

        filterChain.doFilter(request, response);
    }

    private static boolean isRequestSummaryURI(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/post") && request.getMethod().equals(HttpMethod.POST.name());
    }

    private static boolean isRequestPassURI(HttpServletRequest request) {
        if (request.getRequestURI().equals("/")) {
            return true;
        }

        if (request.getRequestURI().startsWith("/v1/auth")) {
            return true;
        }

        if (request.getRequestURI().startsWith("/v1/exception")) {
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
