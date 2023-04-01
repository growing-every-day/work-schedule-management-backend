package fastcampus.workschedulemanagementbackend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "가입하시려는 아이디가 이미 존재합니다 :( 다른 아이디로 가입해주세요!"),
    JWT_EXPIRED(HttpStatus.BAD_REQUEST, "1"),
    JWT_WRONG(HttpStatus.BAD_REQUEST, "2");

    private HttpStatus status;
    private String message;
}

