package project.backend.business.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.auth.implement.KakaoLoginManager;
import project.backend.common.auth.token.BlacklistToken;
import project.backend.business.auth.request.TokenServiceRequest;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.repository.auth.BlacklistTokenRedisRepository;
import project.backend.repository.auth.RefreshTokenRedisRepository;
import project.backend.entity.user.User;
import project.backend.common.auth.token.RefreshToken;
import project.backend.business.auth.implement.TokenProvider;
import project.backend.business.auth.response.TokenServiceResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final TokenProvider tokenProvider;
  private final KakaoLoginManager kakaoLoginManager;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final BlacklistTokenRedisRepository blacklistTokenRedisRepository;

  @Transactional
  public TokenServiceResponse kakaoLogin(String code) throws JsonProcessingException {
    String token = kakaoLoginManager.getKakaoToken(code);
    User user = kakaoLoginManager.getKakaoUser(token);

    TokenServiceResponse tokenServiceResponse = tokenProvider.createToken(
        String.valueOf(user.getId()), user.getEmail(), "USER");

    saveRefreshTokenOnRedis(user, tokenServiceResponse);

    return tokenServiceResponse;
  }

  @Transactional
  public void logout(TokenServiceRequest tokenServiceRequest) {
    String accessToken = tokenServiceRequest.getAccessToken();

    if (accessToken == null || !tokenProvider.validate(accessToken)) {
      throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
    }

    long expiration = tokenProvider.getExpiration(accessToken);

    blacklistTokenRedisRepository.save(BlacklistToken.builder()
                                                     .token(accessToken)
                                                     .expiration(expiration / 1000)
                                                     .build());

    Authentication authentication = tokenProvider.getAuthentication(accessToken);
    String userId = authentication.getName();

    refreshTokenRedisRepository.deleteById(userId);

    SecurityContextHolder.clearContext();
  }

  public TokenServiceResponse reissueAccessToken(TokenServiceRequest tokenServiceRequest) {
    String refreshToken = tokenServiceRequest.getRefreshToken();

    // RefreshToken 이 유효하지 않을 경우
    if (!tokenProvider.validate(refreshToken) || !tokenProvider.validateExpired(refreshToken)) {
      throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

    TokenServiceResponse tokenServiceResponse = tokenProvider.createToken(
        String.valueOf(findToken.getId()),
        findToken.getEmail(),
        findToken.getAuthority());

    refreshTokenRedisRepository.save(RefreshToken.builder()
                                                 .id(findToken.getId())
                                                 .email(findToken.getEmail())
                                                 .authorities(findToken.getAuthorities())
                                                 .refreshToken(tokenServiceResponse.getRefreshToken())
                                                 .build());

    SecurityContextHolder.getContext()
                         .setAuthentication(
                             tokenProvider.getAuthentication(tokenServiceResponse.getAccessToken()));

    return tokenServiceResponse;
  }

  private void saveRefreshTokenOnRedis(User user, TokenServiceResponse response) {
    refreshTokenRedisRepository.save(RefreshToken.builder()
                                                 .id(user.getId())
                                                 .email(user.getEmail())
                                                 .authorities(Collections.singleton(
                                                     new SimpleGrantedAuthority("USER")))
                                                 .refreshToken(response.getRefreshToken())
                                                 .build());
  }
}
