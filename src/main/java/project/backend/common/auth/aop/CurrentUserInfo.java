package project.backend.common.auth.aop;

import jakarta.validation.constraints.Null;

public class CurrentUserInfo {

  @Null
  private Long userId;
}
