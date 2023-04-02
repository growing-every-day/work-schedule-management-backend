package fastcampus.workschedulemanagementbackend.common.error.exception;

import fastcampus.workschedulemanagementbackend.common.error.ErrorCode;

public class InvalidUsernamePasswordException extends RuntimeException {
    private ErrorCode errorCode;

    public InvalidUsernamePasswordException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
