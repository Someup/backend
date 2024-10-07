package project.backend.business.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KakaoLoginServiceRequest {
  private final String code;
  private final String redirectUri;
}
