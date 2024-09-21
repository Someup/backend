package project.backend.business.auth.service;

import static project.backend.business.auth.service.implement.KakaoLoginManager.BEARER;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.auth.service.implement.KakaoLoginManager;
import project.backend.repository.auth.RefreshTokenRedisRepository;
import project.backend.entity.user.User;
import project.backend.common.auth.token.RefreshToken;
import project.backend.common.auth.token.TokenProvider;
import project.backend.common.auth.token.TokenResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final TokenProvider tokenProvider;
  private final KakaoLoginManager kakaoLoginManager;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;

  @Value("${jwt.refresh_header}")
  private String refreshTokenHeader;

  @Transactional
  public TokenResponse kakaoLogin(String code) throws JsonProcessingException {
    String token = kakaoLoginManager.getKakaoToken(code);
    User user = kakaoLoginManager.getKakaoUser(token);

    TokenResponse tokenResponse = tokenProvider.createToken(
        String.valueOf(user.getId()), user.getEmail(), "USER");

    saveRefreshTokenOnRedis(user, tokenResponse);

    return tokenResponse;
  }

  public TokenResponse reissueAccessToken(HttpServletRequest request) {
    String refreshToken = extractRefreshToken(request).orElse(null);

    // RefreshToken 이 유효하지 않을 경우
    if (!tokenProvider.validate(refreshToken) || !tokenProvider.validateExpired(refreshToken)) {
      throw new RuntimeException();
    }

    RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

    TokenResponse tokenResponse = tokenProvider.createToken(
        String.valueOf(findToken.getId()),
        findToken.getEmail(),
        findToken.getAuthority());

    refreshTokenRedisRepository.save(RefreshToken.builder()
                                                 .id(findToken.getId())
                                                 .email(findToken.getEmail())
                                                 .authorities(findToken.getAuthorities())
                                                 .refreshToken(tokenResponse.getRefreshToken())
                                                 .build());

    SecurityContextHolder.getContext()
                         .setAuthentication(
                             tokenProvider.getAuthentication(tokenResponse.getAccessToken()));

    return tokenResponse;
  }

  private void saveRefreshTokenOnRedis(User user, TokenResponse response) {
    refreshTokenRedisRepository.save(RefreshToken.builder()
                                                 .id(user.getId())
                                                 .email(user.getEmail())
                                                 .authorities(Collections.singleton(
                                                     new SimpleGrantedAuthority("USER")))
                                                 .refreshToken(response.getRefreshToken())
                                                 .build());
  }

  // RefreshToken 추출
  private Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshTokenHeader))
                   .filter(refreshToken -> refreshToken.startsWith(BEARER))
                   .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }
}
