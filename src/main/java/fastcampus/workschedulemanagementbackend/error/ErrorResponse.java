package fastcampus.workschedulemanagementbackend.error;

public record ErrorResponse(Integer errorCode, String errorMessage) {

    public static ErrorResponse of(Integer errorCode, String errorMessage) {
        return new ErrorResponse(errorCode, errorMessage);
    }

}
