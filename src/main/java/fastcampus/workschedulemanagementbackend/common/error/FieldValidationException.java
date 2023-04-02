package fastcampus.workschedulemanagementbackend.common.error;

import fastcampus.workschedulemanagementbackend.dto.ValidationErrorDto;
import lombok.Getter;

@Getter
public class FieldValidationException extends RuntimeException {
    private ErrorCode errorCode;
    private ValidationErrorDto validationErrorDto;

    public FieldValidationException(ErrorCode errorCode, ValidationErrorDto validationErrorDto) {
        this.errorCode = errorCode;
        this.validationErrorDto = validationErrorDto;
    }
}
