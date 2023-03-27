package fastcampus.workschedulemanagementbackend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class wsAppException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    @Override
    public String getMessage(){
        if(message == null){
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
