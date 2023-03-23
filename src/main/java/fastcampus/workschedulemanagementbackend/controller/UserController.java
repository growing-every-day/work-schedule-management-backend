package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO : implement
    @PostMapping("/signup")
    public void join(){
        //join
        userService.join("","","","");
    }
}
