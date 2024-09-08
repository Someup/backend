package project.backend.global.error.exception;

public class UserNotAuthenticatedException extends RuntimeException {

  public UserNotAuthenticatedException() {
    super("인증된 유저 정보를 찾을 수 없습니다.");
  }

  public UserNotAuthenticatedException(String message) {
    super(message);
  }

  public UserNotAuthenticatedException(String message, Throwable cause) {
    super(message, cause);
  }
}
