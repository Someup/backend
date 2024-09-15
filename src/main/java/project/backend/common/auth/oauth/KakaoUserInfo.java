package project.backend.common.auth.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class KakaoUserInfo {

  public static final String KAKAO_ACCOUNT = "kakao_account";
  public static final String EMAIL = "email";
  public static final String PROFILE = "profile";
  public static final String NAME = "nickname";
  public static final String PROFILE_IMAGE_URL = "profile_image_url";

  private final Map<String, Object> attributes;

  public KakaoUserInfo(final Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public String getEmail() {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<Map<String, Object>> typeReferencer = new TypeReference<>() {
    };

    Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
    Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);

    return (String) account.get(EMAIL);
  }

  public String getName() {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<Map<String, Object>> typeReferencer = new TypeReference<>() {
    };

    Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
    Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);
    Map<String, Object> profile = objectMapper.convertValue(account.get(PROFILE), typeReferencer);

    return (String) profile.get(NAME);
  }

  public String getProfileImageUrl() {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<Map<String, Object>> typeReferencer = new TypeReference<>() {
    };

    Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
    Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);
    Map<String, Object> profile = objectMapper.convertValue(account.get(PROFILE), typeReferencer);

    return (String) profile.get(PROFILE_IMAGE_URL);
  }
}
