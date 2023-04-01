package fastcampus.workschedulemanagementbackend.error;

import fastcampus.workschedulemanagementbackend.dto.response.useraccount.Response;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(wsAppException.class)
    public ResponseEntity<?> applicationHandler(wsAppException e){
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

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
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> jwtExpiredHandler(ExpiredJwtException e){
        log.error("Error occurs {}", e.getCause());
        return new ResponseEntity<>(
                ErrorCode.JWT_EXPIRED.getMessage(),
                ErrorCode.JWT_EXPIRED.getStatus()
        );
    }

}
