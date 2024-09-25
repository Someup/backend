package project.backend.business.auth.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenServiceResponse {

  private final String accessToken;
  private final String refreshToken;

  public static TokenServiceResponse of(final String accessToken, final String refreshToken) {
    return new TokenServiceResponse(accessToken, refreshToken);
  }
}
