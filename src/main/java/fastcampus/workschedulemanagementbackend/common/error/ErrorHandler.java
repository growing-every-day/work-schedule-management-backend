package fastcampus.workschedulemanagementbackend.common.error;

import fastcampus.workschedulemanagementbackend.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        log.error("Error occurs {}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.of(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage()),
                ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(InvalidRemainedVacationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRemainedVacationException(InvalidRemainedVacationException ex) {
        log.error("Error occurs {}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.of(ex.getErrorCode().getCode(), ex.getMessage()),
                ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ErrorResponse> handleFieldValidationException(FieldValidationException ex) {
        log.error("Error occurs {}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.of(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage(), ex.getValidationErrorDto().fieldErrors()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Error occurs {}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage()),
                ErrorCode.SERVER_ERROR.getStatus()
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> jwtExpiredHandler(ExpiredJwtException e){
        log.error("Error occurs {}", e.getCause());
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.JWT_EXPIRED.getCode(), ErrorCode.JWT_EXPIRED.getMessage()),
                ErrorCode.JWT_EXPIRED.getStatus()
        );
    }

}
