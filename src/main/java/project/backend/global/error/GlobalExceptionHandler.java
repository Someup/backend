package project.backend.global.error;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.backend.global.error.exception.UserNotAuthenticatedException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotAuthenticatedException.class)
  public ResponseEntity<ErrorResponse> handleUserNotAuthenticatedException(UserNotAuthenticatedException e, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        e.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }
}
