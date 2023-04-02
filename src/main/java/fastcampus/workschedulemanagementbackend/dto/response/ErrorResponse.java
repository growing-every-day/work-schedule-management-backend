package fastcampus.workschedulemanagementbackend.dto.response;

import java.util.List;

public record ErrorResponse(Integer errorCode, String errorMessage, List fieldErrors) {

    public static ErrorResponse of(Integer errorCode, String errorMessage) {
        return new ErrorResponse(errorCode, errorMessage, null);
    }

    public static ErrorResponse of(Integer errorCode, String errorMessage, List fieldErrors) {
        return new ErrorResponse(errorCode, errorMessage, fieldErrors);
    }



}
