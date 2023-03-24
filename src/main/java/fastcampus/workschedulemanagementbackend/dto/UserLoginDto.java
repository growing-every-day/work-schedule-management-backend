package fastcampus.workschedulemanagementbackend.dto;
import lombok.Getter;
import lombok.ToString;

/**
 * 비즈니스 로직 중 '로그인'에 사용이 되는 Model 객체를 생성
 */
@ToString
@Getter
public class UserLoginDto {

    private String userName; // 아이디
    private String pwd; // 패스워드

    protected UserLoginDto() {
    }

    private UserLoginDto(String userName) {
        this.userName = userName;
    }

    private UserLoginDto(String userName, String pwd) {
        this.userName = userName;
        this.pwd = pwd;
    }

    public static UserLoginDto of(String userName) {
        return new UserLoginDto(userName);
    }
    public static UserLoginDto of(String userName, String pwd) {
        return new UserLoginDto(userName, pwd);
    }
}
