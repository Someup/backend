package project.backend.business.auth.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import project.backend.security.oauth.KakaoUserInfo;
import project.backend.common.error.exception.UserNotAuthenticatedException;
import project.backend.entity.user.User;
import project.backend.repository.user.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLoginManager {

  public static final String BEARER = "Bearer ";

  private final UserRepository userRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
  private String kakaoClientId;

  @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
  private String kakaoClientSecret;

  public String getKakaoToken(String code, String redirectUri) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", kakaoClientId);
    body.add("client_secret", kakaoClientSecret);
    body.add("redirect_uri", redirectUri);
    body.add("code", code);

    HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
    ResponseEntity<String> response = restTemplate.exchange(
        "https://kauth.kakao.com/oauth/token",
        HttpMethod.POST,
        kakaoTokenRequest,
        String.class
    );

    JsonNode jsonNode = objectMapper.readTree(response.getBody());
    return jsonNode.get("access_token").asText();
  }

  @Transactional
  public User getKakaoUser(String token) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", BEARER + token);
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    HttpEntity<Void> kakaoProfileRequest = new HttpEntity<>(headers);

    try {
      ResponseEntity<String> response = restTemplate.exchange(
          "https://kapi.kakao.com/v2/user/me",
          HttpMethod.POST,
          kakaoProfileRequest,
          String.class
      );

      String responseBody = response.getBody();
      Map<String, Object> attributes = objectMapper.readValue(responseBody, new TypeReference<>() {});

      KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

      String email = kakaoUserInfo.getEmail();
      String name = kakaoUserInfo.getName();
      String profileImageUrl = kakaoUserInfo.getProfileImageUrl();

      return userRepository.findByEmailAndActivatedTrue(email)
                           .orElseGet(() -> userRepository.save(
                               User.createUser(email, name, profileImageUrl)
                           ));
    } catch (HttpClientErrorException e) {
      throw new UserNotAuthenticatedException("카카오 사용자 정보를 가져오는데 실패했습니다.", e);
    }
  }
}
