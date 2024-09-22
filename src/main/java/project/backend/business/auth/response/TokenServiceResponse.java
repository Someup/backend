package project.backend.business.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenServiceResponse {

  private final String accessToken;
  @JsonIgnore
  private final String refreshToken;

  public static TokenServiceResponse of(final String accessToken, final String refreshToken) {
    return new TokenServiceResponse(accessToken, refreshToken);
  }
}
