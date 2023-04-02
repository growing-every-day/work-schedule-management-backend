package fastcampus.workschedulemanagementbackend.dto.response;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.dto.TokenDto;
import fastcampus.workschedulemanagementbackend.common.utils.AESUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class LoginResponseDto {

    private Long id;
    private String userName;
    private String name;
    private String email;
    private UserRoleType role;
    private TokenDto token;

    private LoginResponseDto(Long id, String userName, String name, String email, UserRoleType role, TokenDto token) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public static LoginResponseDto from(UserAccount userAccount, TokenDto token, AESUtil aesUtil) {
        return new LoginResponseDto(
                userAccount.getId(),
                userAccount.getUsername(),
                aesUtil.decrypt(userAccount.getName()),
                aesUtil.decrypt(userAccount.getEmail()),
                userAccount.getRole(),
                token
        );
    }

}
