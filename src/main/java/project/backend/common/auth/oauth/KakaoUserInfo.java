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

  private Map<String, Object> getAccountAttribute(String key) {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {};
    Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
    Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReference);
    return objectMapper.convertValue(account.get(key), typeReference);
  }

  public String getEmail() {
    return (String) getAccountAttribute(EMAIL).get(EMAIL);
  }

  public String getName() {
    return (String) getAccountAttribute(PROFILE).get(NAME);
  }

  public String getProfileImageUrl() {
    return (String) getAccountAttribute(PROFILE).get(PROFILE_IMAGE_URL);
  }
}
