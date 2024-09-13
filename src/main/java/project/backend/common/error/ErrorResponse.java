package project.backend.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

  private final int status;
  private final String error;
  private final String message;
}
