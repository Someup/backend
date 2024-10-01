package project.backend.business.user.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoResponse {

  private final String name;
  private final String email;
  private final String profileImageUrl;
}
