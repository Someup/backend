package project.backend.common.auth.aop;

import jakarta.validation.constraints.Null;
import lombok.Getter;

@Getter
public class CurrentUserInfo {

  @Null
  private Long userId;
}
