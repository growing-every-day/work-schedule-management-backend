package fastcampus.workschedulemanagementbackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

/**
 * 비즈니스 로직 중 '로그인'에 사용이 되는 Model 객체를 생성
 */
@ToString
@Getter
public class LoginRequestDto {

    @Pattern(regexp = "^[a-z0-9]{5,10}$", message = "아이디는 5~10자 영문 소문자, 숫자로 입력하세요.")
    private String username; // 아이디
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
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
