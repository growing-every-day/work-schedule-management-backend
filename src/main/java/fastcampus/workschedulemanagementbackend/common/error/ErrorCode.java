package fastcampus.workschedulemanagementbackend.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    JWT_EXPIRED(1, HttpStatus.BAD_REQUEST, "토큰이 만료됐습니다."),
    JWT_WRONG(2, HttpStatus.BAD_REQUEST, "토큰값이 올바르지 않습니다."),
    NO_ADMIN(3, HttpStatus.BAD_REQUEST, "다른 회원을 삭제할 권한이 없습니다."),
    USER_ID_NULL(4, HttpStatus.BAD_REQUEST, "회원 번호는 null일 수 없습니다."),
    USER_NOT_FOUND(5, HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    FIELD_VALIDATION_FAILED(6, HttpStatus.BAD_REQUEST, "입력한 값이 올바르지 않습니다."),
    USER_ACCOUNT_NULL(7, HttpStatus.BAD_REQUEST, "회원 정보는 null일 수 없습니다."),
    USER_ACCOUNT_QUERY_FAILED(8, HttpStatus.INTERNAL_SERVER_ERROR, "회원 조회에 실패했습니다."),
    USER_ACCOUNT_UPDATE_FAILED(9, HttpStatus.INTERNAL_SERVER_ERROR, "회원 수정에 실패했습니다."),
    USER_ACCOUNT_DELETE_FAILED(10, HttpStatus.INTERNAL_SERVER_ERROR, "회원 삭제에 실패했습니다."),
    DUPLICATED_USER_NAME(11, HttpStatus.BAD_REQUEST, "가입하시려는 아이디가 이미 존재합니다 :( 다른 아이디로 가입해주세요!"),
    AUTHENTICATION_WRONG(12, HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    AUTHORIZATION_WRONG(13, HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    NO_EVENT_ID(14, HttpStatus.BAD_REQUEST, "eventId 정보를 받아오는데 실패했습니다."),
    SCHEDULE_NOT_FOUND(15, HttpStatus.BAD_REQUEST, "스케줄 정보가 존재하지 않습니다."),
    INVALID_REMAINED_VACATION(17, HttpStatus.BAD_REQUEST, "사용가능한 휴가일 수가 부족합니다."),
    ALREADY_IN_THE_SCHEDULE(16, HttpStatus.BAD_REQUEST, "휴가, 당직 중에 일정은 변경할 수 없습니다."),
    INVALID_USER_ROLE_TYPE(18, HttpStatus.BAD_REQUEST, "유효하지 않은 유저 권한 타입입니다."),
    NO_ADMIN_NO_OWN_SCHEDULE(19, HttpStatus.BAD_REQUEST, "다른 회원의 스케쥴을 수정할 권한이 없습니다."),
    DUPLICATED_SCHEDULE(20, HttpStatus.BAD_REQUEST, "요청한 날짜가 이미 신청한 당직 또는 휴가 날짜와 중복됩니다."),
    SERVER_ERROR(99,HttpStatus.INTERNAL_SERVER_ERROR ,"서버 오류로 정상적으로 요청을 처리할 수 없습니다.");

    private int code;           // 프론트엔드 팀과 합의한 정수형 에러코드
    private HttpStatus status;  // reponse status 값
    private String message;     // 오류에 관련한 메세지
}

