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
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {};

  public KakaoUserInfo(final Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  private Map<String, Object> convertToMap(Object object) {
    return objectMapper.convertValue(object, typeReference);
  }

  private Map<String, Object> getKakaoAccount() {
    return convertToMap(attributes.get(KAKAO_ACCOUNT));
  }

  private Map<String, Object> getProfileAttributes() {
    Map<String, Object> account = getKakaoAccount();
    return convertToMap(account.get(PROFILE));
  }

  public String getEmail() {
    Map<String, Object> account = getKakaoAccount();
    return (String) account.get(EMAIL);
  }

  public String getName() {
    Map<String, Object> profile = getProfileAttributes();
    return (String) profile.get(NAME);
  }

  public String getProfileImageUrl() {
    Map<String, Object> profile = getProfileAttributes();
    return (String) profile.get(PROFILE_IMAGE_URL);
  }
}
