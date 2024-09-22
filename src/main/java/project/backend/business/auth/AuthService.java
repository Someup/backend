package project.backend.business.auth;

import static project.backend.business.auth.implement.KakaoLoginManager.BEARER;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.auth.implement.KakaoLoginManager;
import project.backend.common.auth.token.BlacklistToken;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.repository.auth.BlacklistTokenRedisRepository;
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
  private final BlacklistTokenRedisRepository blacklistTokenRedisRepository;

  @Value("${jwt.access_header}")
  private String accessTokenHeader;
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

  @Transactional
  public void logout(HttpServletRequest request) {
    String accessToken = extractAccessToken(request).orElse(null);

    log.info("로그아웃 요청 수신. Access Token: {}", accessToken);

    if (accessToken != null && tokenProvider.validate(accessToken)) {
      long expiration = tokenProvider.getExpiration(accessToken);
      log.info("Access Token 유효함. 블랙리스트에 추가. 만료까지 남은 시간: {}초", expiration / 1000);

      // 엑세스 토큰을 블랙리스트에 추가
      blacklistTokenRedisRepository.save(BlacklistToken.builder()
                                                       .token(accessToken)
                                                       .expiration(expiration / 1000)
                                                       .build());

      // Access Token에서 사용자 정보 추출
      Authentication authentication = tokenProvider.getAuthentication(accessToken);
      String userId = authentication.getName(); // 사용자 ID

      // Redis에서 리프레시 토큰 삭제
      refreshTokenRedisRepository.deleteById(userId);
      log.info("사용자 ID {}에 대한 Refresh Token 이 삭제되었습니다.", userId);
    } else {
      log.warn("Access Token 이 유효하지 않거나 null 입니다.");
    }

    SecurityContextHolder.clearContext();
    log.info("SecurityContextHolder 가 초기화되었습니다.");
  }

  public TokenResponse reissueAccessToken(HttpServletRequest request) {
    String refreshToken = extractRefreshToken(request).orElse(null);

    // RefreshToken 이 유효하지 않을 경우
    if (!tokenProvider.validate(refreshToken) || !tokenProvider.validateExpired(refreshToken)) {
      throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
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

  // AccessToken 추출
  private Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessTokenHeader))
                   .filter(token -> token.startsWith(BEARER))
                   .map(token -> token.replace(BEARER, ""));
  }

  // RefreshToken 추출
  private Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshTokenHeader))
                   .filter(refreshToken -> refreshToken.startsWith(BEARER))
                   .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }
}
