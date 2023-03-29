package fastcampus.workschedulemanagementbackend.dto;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor
public class LoginResponseDto {

    private Long id;

    private String userName;

    private String name;

    private String email;

    private UserRoleType role = UserRoleType.USER;

    private TokenDto token;

    public LoginResponseDto(UserAccount userAccount) {
        this.id = userAccount.getId();
        this.userName = userAccount.getUserName();
        this.name = userAccount.getName();
        this.email = userAccount.getEmail();
        this.role = userAccount.getRole();
    }
}
