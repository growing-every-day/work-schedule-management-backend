package fastcampus.workschedulemanagementbackend.dto.response.useraccount;

import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountJoinResponse {

    private Long id;
    private String username;
    private UserRoleType role;

    public static UserAccountJoinResponse fromWithoutUser(UserAccountDto user){
        return new UserAccountJoinResponse(

                user.id(), user.username(), user.role()
        );
    }

}
