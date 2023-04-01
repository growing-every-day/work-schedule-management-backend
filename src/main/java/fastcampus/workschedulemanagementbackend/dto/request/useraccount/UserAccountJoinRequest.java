package fastcampus.workschedulemanagementbackend.dto.request.useraccount;

import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountJoinRequest {

    @Pattern(regexp = "^[a-z0-9]{5,10}$", message = "아이디는 5~10자 영문 소문자, 숫자로 입력하세요.")
    private String username;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 2~5자 한글로 입력하세요.")
    private String name;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;


    public static UserAccountJoinRequest of(String username, String password, String name, String email) {
        return new UserAccountJoinRequest(username, password, name, email);
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(username, password, name, email);
    }

}
