package project.backend.business.auth.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class TokenServiceRequest {
  private String accessToken;
  private String refreshToken;
}
