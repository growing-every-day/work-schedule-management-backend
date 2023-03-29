package fastcampus.workschedulemanagementbackend.dto;
import lombok.Getter;
import lombok.ToString;

/**
 * 비즈니스 로직 중 '로그인'에 사용이 되는 Model 객체를 생성
 */
@ToString
@Getter
public class LoginRequestDto {

    private String username; // 아이디
    private String password; // 패스워드

    protected LoginRequestDto() {
    }

    private LoginRequestDto(String username) {
        this.username = username;
    }

    private LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static LoginRequestDto of(String userName) {
        return new LoginRequestDto(userName);
    }
    public static LoginRequestDto of(String userName, String pwd) {
        return new LoginRequestDto(userName, pwd);
    }
}
