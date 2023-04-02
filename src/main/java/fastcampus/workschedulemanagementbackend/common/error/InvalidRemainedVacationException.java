package fastcampus.workschedulemanagementbackend.common.error;

import lombok.Getter;
@Getter
public class InvalidRemainedVacationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public InvalidRemainedVacationException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
