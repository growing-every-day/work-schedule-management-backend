package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // TODO : implement
    @PostMapping("/signup")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        //join
        User user = userService.join(request.getUserName(), request.getPassword(), request.getEmail(), request.getName());
        return Response.success(UserJoinResponse.fromUser(user));
    }
}
