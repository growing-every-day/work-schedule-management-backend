package fastcampus.workschedulemanagementbackend.common.error.exception;

import fastcampus.workschedulemanagementbackend.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private ErrorCode errorCode;

    public BadRequestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
