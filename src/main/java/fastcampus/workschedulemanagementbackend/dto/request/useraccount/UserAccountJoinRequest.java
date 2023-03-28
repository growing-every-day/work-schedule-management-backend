package fastcampus.workschedulemanagementbackend.dto.request.useraccount;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountJoinRequest {

    private String username;
    private String password;
    private String name;

    private String email;



}
