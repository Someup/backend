package project.backend.common.error;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_AUTHENTICATED("유효하지 않는 인증 정보입니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("올바르지 않은 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST("잘못된 접근입니다.", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}