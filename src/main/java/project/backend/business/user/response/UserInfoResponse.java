package project.backend.business.user.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private final String name;
  private final String email;
  private final String profileImageUrl;
}
