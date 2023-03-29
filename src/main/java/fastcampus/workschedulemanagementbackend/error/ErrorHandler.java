package fastcampus.workschedulemanagementbackend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {

        return new ResponseEntity<>(
                ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getErrorMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ErrorResponse> handleFieldValidationException(FieldValidationException ex) {

        return new ResponseEntity<>(
                ErrorResponse.of(HttpStatus.BAD_REQUEST.value(),ex.getErrorMessage(), ex.getValidationErrorDto().fieldErrors()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        return new ResponseEntity<>(
                ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server Error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
