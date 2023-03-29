package fastcampus.workschedulemanagementbackend.error;

import fastcampus.workschedulemanagementbackend.dto.ValidationErrorDto;

public class FieldValidationException extends RuntimeException {
    private String errorMessage;
    private ValidationErrorDto validationErrorDto;

    public FieldValidationException(String errorMessage,ValidationErrorDto validationErrorDto) {
        this.errorMessage = errorMessage;
        this.validationErrorDto = validationErrorDto;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ValidationErrorDto getValidationErrorDto() {
        return validationErrorDto;
    }
}
