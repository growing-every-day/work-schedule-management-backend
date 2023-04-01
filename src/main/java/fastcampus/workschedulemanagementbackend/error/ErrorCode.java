package fastcampus.workschedulemanagementbackend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated"),
    JWT_EXPIRED(HttpStatus.BAD_REQUEST, "1"),
    JWT_WRONG(HttpStatus.BAD_REQUEST, "2");

    private HttpStatus status;
    private String message;
}

