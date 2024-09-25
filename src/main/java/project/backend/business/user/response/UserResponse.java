package project.backend.business.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

  private final String name;
  private final String email;
  private final String profileImageUrl;
}
