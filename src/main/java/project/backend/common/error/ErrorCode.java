package project.backend.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  NONE_AUTHENTICATED("인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
  NOT_AUTHENTICATED("유효하지 않는 인증 정보입니다.", HttpStatus.UNAUTHORIZED),
  USER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
  BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
  INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
  INVALID_ACCESS_TOKEN("유효하지 않은 엑세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_INVALID("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
  ACCESS_DENIED("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
  NOT_EXIST_REFRESH_TOKEN("존재하지 않는 리프레시 토큰입니다.", HttpStatus.BAD_REQUEST);

  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
