package project.backend.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import project.backend.user.domain.User;
import project.backend.user.infra.security.jwt.token.RefreshToken;
import project.backend.user.infra.security.jwt.token.TokenProvider;
import project.backend.user.infra.security.jwt.token.TokenResponse;
import project.backend.user.repositoy.RefreshTokenRedisRepository;
import project.backend.user.repositoy.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final String BEARER = "Bearer ";
  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;

  @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
  private String kakaoClientId;

  @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
  private String kakaoClientSecret;

  @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
  private String redirect_uri;

  @Value("${jwt.refresh_header}")
  private String refreshTokenHeader;

  @Transactional
  public TokenResponse kakaoLogin(String code) throws JsonProcessingException {
    String token = getToken(code);
    String email = getKakaoUserInfo(token);
    User user = saveIfNonExist(email);

    TokenResponse tokenResponse = tokenProvider.createToken(
        String.valueOf(user.getId()), user.getEmail(), "USER"
    );

    saveRefreshTokenOnRedis(user, tokenResponse);

    return tokenResponse;
  }

  private void saveRefreshTokenOnRedis(User user, TokenResponse response) {
    refreshTokenRedisRepository.save(RefreshToken.builder()
                                                 .id(user.getId())
                                                 .email(user.getEmail())
                                                 .authorities(
                                                     Collections.singleton(new SimpleGrantedAuthority("USER")))
                                                 .refreshToken(response.getRefreshToken())
                                                 .build());
  }

  private String getToken(final String code) throws JsonProcessingException {
    // HTTP 헤더 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // HTTP 바디 생성
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", kakaoClientId);
    body.add("client_secret", kakaoClientSecret);
    body.add("redirect_uri", redirect_uri);
    body.add("code", code);

    // HTTP 요청 보내기
    HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(
        "https://kauth.kakao.com/oauth/token",
        HttpMethod.POST,
        kakaoTokenRequest,
        String.class
    );

    String responseBody = response.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);

    return jsonNode.get("access_token").asText();
  }

  private String getKakaoUserInfo(String token) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();

    headers.add("Authorization", "Bearer " + token);
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(headers);

    try {
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> response = restTemplate.exchange(
          "https://kapi.kakao.com/v2/user/me",
          HttpMethod.POST,
          kakaoTokenRequest,
          String.class
      );
      String responseBody = response.getBody();
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(responseBody);
      return jsonNode.get("kakao_account").get("email").asText();
    } catch (HttpClientErrorException e) {
      throw new RuntimeException();
    }
  }

  @Transactional
  public User saveIfNonExist(String email) {
    return userRepository.findByEmail(email)
                         .orElseGet(() ->
                             userRepository.save(
                                 User.createUser(email, "낯선 " + email.split("@")[0])
                             )
                         );
  }

  public TokenResponse reissueAccessToken(HttpServletRequest request) {
    String refreshToken = extractRefreshToken(request).orElse(null);

    // RefreshToken 이 유효하지 않을 경우 -> 재로그인 필요
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
                         .setAuthentication(tokenProvider.getAuthentication(tokenResponse.getAccessToken()));

    return tokenResponse;
  }

  // RefreshToken 추출
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshTokenHeader))
                   .filter(refreshToken -> refreshToken.startsWith(BEARER))
                   .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }
}
