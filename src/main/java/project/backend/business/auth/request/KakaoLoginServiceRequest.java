package project.backend.business.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginServiceRequest {
  private final String code;
  private final String redirectUri;
}
