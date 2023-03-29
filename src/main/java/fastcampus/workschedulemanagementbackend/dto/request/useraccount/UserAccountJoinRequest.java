package fastcampus.workschedulemanagementbackend.dto.request.useraccount;

import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountJoinRequest {

    private String username;
    private String password;
    private String name;
    private String email;


    public static UserAccountJoinRequest of(String username, String password, String name, String email) {
        return new UserAccountJoinRequest(username, password, name, email);
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(username, password, name, email);
    }

}
