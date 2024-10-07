package project.backend.business.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.auth.implement.KakaoLoginManager;
import project.backend.business.auth.request.KakaoLoginServiceRequest;
import project.backend.business.user.implement.UserManager;
import project.backend.business.user.implement.UserReader;
import project.backend.entity.token.BlacklistToken;
import project.backend.business.auth.request.TokenServiceRequest;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.repository.auth.BlacklistTokenRedisRepository;
import project.backend.repository.auth.RefreshTokenRedisRepository;
import project.backend.entity.user.User;
import project.backend.entity.token.RefreshToken;
import project.backend.business.auth.implement.TokenProvider;
import project.backend.business.auth.response.TokenServiceResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final KakaoLoginManager kakaoLoginManager;
  private final UserManager userManager;
  private final UserReader userReader;
  private final TokenProvider tokenProvider;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final BlacklistTokenRedisRepository blacklistTokenRedisRepository;

  @Transactional
  public TokenServiceResponse kakaoLogin(KakaoLoginServiceRequest serviceRequest) throws JsonProcessingException {
    String token = kakaoLoginManager.getKakaoToken(serviceRequest.getCode(), serviceRequest.getRedirectUri());
    User user = kakaoLoginManager.getKakaoUser(token);

    TokenServiceResponse tokenServiceResponse = tokenProvider.createToken(
        String.valueOf(user.getId()), user.getEmail(), "USER");

    saveRefreshTokenOnRedis(user, tokenServiceResponse);

    return tokenServiceResponse;
  }

  @Transactional
  public void logout(TokenServiceRequest tokenServiceRequest) {
    invalidateTokens(tokenServiceRequest);
  }

  @Transactional
  public void withdraw(Long userId, TokenServiceRequest tokenServiceRequest) {
    User user = userReader.readUserById(userId);
    userManager.withdrawUser(user);
    invalidateTokens(tokenServiceRequest);
  }

  @Transactional
  public TokenServiceResponse reissueAccessToken(TokenServiceRequest tokenServiceRequest) {
    String refreshToken = tokenServiceRequest.getRefreshToken();

    // RefreshToken 이 유효하지 않을 경우
    if (!tokenProvider.validate(refreshToken) || !tokenProvider.validateExpired(refreshToken)) {
      throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    RefreshToken findToken = refreshTokenRedisRepository.findById(refreshToken)
                                                        .orElseThrow(() -> new CustomException(
                                                            ErrorCode.NOT_EXIST_REFRESH_TOKEN));
    refreshTokenRedisRepository.deleteById(refreshToken);

    // 새 AccessToken 생성
    TokenServiceResponse tokenServiceResponse = tokenProvider.createToken(
        String.valueOf(findToken.getId()),
        findToken.getEmail(),
        findToken.getAuthority()
    );

    // 새로 발급된 RefreshToken 을 다시 저장
    refreshTokenRedisRepository.save(
        RefreshToken.builder()
                    .id(findToken.getId())
                    .email(findToken.getEmail())
                    .authorities(findToken.getAuthorities())
                    .refreshToken(tokenServiceResponse.getRefreshToken())
                    .build()
    );

    SecurityContextHolder.getContext()
                         .setAuthentication(
                             tokenProvider.getAuthentication(tokenServiceResponse.getAccessToken())
                         );

    return tokenServiceResponse;
  }

  private void invalidateTokens(TokenServiceRequest tokenServiceRequest) {
    String accessToken = tokenServiceRequest.getAccessToken();
    String refreshToken = tokenServiceRequest.getRefreshToken();

    if (accessToken == null || !tokenProvider.validate(accessToken)) {
      throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
    }

    long expiration = tokenProvider.getExpiration(accessToken);
    blacklistTokenRedisRepository.save(BlacklistToken.builder()
                                                     .token(accessToken)
                                                     .expiration(expiration / 1000)
                                                     .build());

    refreshTokenRedisRepository.deleteById(refreshToken);

    SecurityContextHolder.clearContext();
  }

  private void saveRefreshTokenOnRedis(User user, TokenServiceResponse response) {
    RefreshToken refreshToken = RefreshToken.builder()
                                            .id(user.getId())
                                            .email(user.getEmail())
                                            .authorities(Collections.singleton(
                                                new SimpleGrantedAuthority("USER")))
                                            .refreshToken(response.getRefreshToken())
                                            .build();
    refreshTokenRedisRepository.save(refreshToken);
  }
}
