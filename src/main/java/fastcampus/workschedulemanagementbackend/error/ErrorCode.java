package fastcampus.workschedulemanagementbackend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(11, HttpStatus.CONFLICT, "가입하시려는 아이디가 이미 존재합니다 :( 다른 아이디로 가입해주세요!"),
    JWT_EXPIRED(1, HttpStatus.BAD_REQUEST, "토큰이 만료됐습니다."),
    JWT_WRONG(2, HttpStatus.BAD_REQUEST, "토큰값이 올바르지 않습니다."),

    AUTHENTICATION_WRONG(12, HttpStatus.BAD_REQUEST, "인증되지 않은 사용자입니다."),
    AUTHORIZATION_WRONG(13, HttpStatus.BAD_REQUEST, "권한이 없는 사용자입니다.");

    private int errorCode;
    private HttpStatus status;
    private String message;
}

