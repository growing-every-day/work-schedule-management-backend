package fastcampus.workschedulemanagementbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRequest {

    private String id;
    private String password;
    private String email;
    private String name;
}
