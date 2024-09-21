package project.backend.common.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    ErrorResponse response = buildResponse(ex.getErrorCode().getHttpStatus(), ex);
    return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
  }

  // 클라이언트의 잘못된 요청으로 발생하는 예외 처리
  @ExceptionHandler({
      IllegalArgumentException.class,         // 잘못된 인수로 인해 발생
      ServletRequestBindingException.class,   // HTTP 요청 바인딩 문제
      HttpMessageNotReadableException.class,  // 읽을 수 없는 HTTP 메시지
      MissingServletRequestPartException.class,   // 요청 일부가 누락된 경우
      MissingServletRequestParameterException.class,  // 요청 파라미터 누락
      TypeMismatchException.class,            // 요청 파라미터 타입 불일치
      BindException.class,                    // 바인딩 오류
      HttpRequestMethodNotSupportedException.class,  // 지원되지 않는 HTTP 메서드 요청
      HttpMediaTypeNotAcceptableException.class,     // 지원되지 않는 미디어 타입 요청
      HttpMediaTypeNotSupportedException.class       // 클라이언트가 지원하지 않는 미디어 타입으로 요청할 때 발생
  })
  public ResponseEntity<ErrorResponse> handleBadRequestAndMediaTypeExceptions(Exception exception) {
    HttpStatus status = determineHttpStatus(exception);
    return ResponseEntity.status(status)
                         .body(buildResponse(status, exception));
  }

  // 존재하지 않는 URL 또는 리소스를 요청할 때 발생하는 예외를 처리
  @ExceptionHandler({
      NoHandlerFoundException.class,      // 요청에 대한 핸들러가 없을 때 발생
      NoResourceFoundException.class      // 요청된 리소스를 찾을 수 없을 때 발생
  })
  public ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception exception) {
    return ResponseEntity
        .status(NOT_FOUND)
        .body(buildResponse(NOT_FOUND, exception));
  }

  // 서버가 요청을 처리할 수 없는 상황에서 발생하는 예외들을 처리합니다.
  @ExceptionHandler({
      MissingPathVariableException.class,     // 경로 변수 누락 시 발생
      ConversionNotSupportedException.class,  // 타입 변환이 지원되지 않을 때 발생
      HttpMessageNotWritableException.class,  // HTTP 메시지를 작성할 수 없을 때 발생
  })
  public ResponseEntity<ErrorResponse> internalServerErrorExceptionHandler(Exception exception) {
    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .body(buildResponse(INTERNAL_SERVER_ERROR, exception));
  }

  // 위에서 처리되지 않은 모든 일반적인 예외를 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception exception) {
    log.error("Unexpected error occurred: {}", exception.getMessage(), exception);  // 스택 트레이스 로깅
    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .body(buildResponse(INTERNAL_SERVER_ERROR, exception));
  }

  // 기본 예외 응답 빌더 메서드
  private ErrorResponse buildResponse(HttpStatus status, Exception exception) {
    return ErrorResponse.builder()
                        .status(status.value())
                        .message(exception.getLocalizedMessage())
                        .build();
  }

  // 유효성 검사 실패 시 발생하는 예외를 처리하며, 설정된 메시지를 반환
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> validationFailedExceptionHandler(MethodArgumentNotValidException exception) {
    return ResponseEntity
        .status(BAD_REQUEST)
        .body(buildValidationFailedResponse(exception));
  }

  // 유효성 검사 실패 응답을 빌드하는 메서드
  private ErrorResponse buildValidationFailedResponse(MethodArgumentNotValidException exception) {
    List<String> errorMessages = exception.getFieldErrors().stream()
                                          .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                          .toList();

    return ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(String.join(", ", errorMessages))
                        .build();
  }

  // 예외의 상태를 결정하는 메서드
  private HttpStatus determineHttpStatus(Exception ex) {
    Map<Class<? extends Exception>, HttpStatus> exceptionStatusMap = new HashMap<>();
    exceptionStatusMap.put(HttpRequestMethodNotSupportedException.class, METHOD_NOT_ALLOWED);
    exceptionStatusMap.put(HttpMediaTypeNotAcceptableException.class, NOT_ACCEPTABLE);
    exceptionStatusMap.put(HttpMediaTypeNotSupportedException.class, UNSUPPORTED_MEDIA_TYPE);

    return exceptionStatusMap.getOrDefault(ex.getClass(), BAD_REQUEST);
  }
}
