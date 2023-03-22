package fastcampus.workschedulemanagementbackend.UserServiceTest;

import fastcampus.workschedulemanagementbackend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void 회원가입이_정상적으로_동작하는_경우(){
        String id = "id";
        String password = "password";
        String email = "example@gmail.com";
        String name = "name";


        Assertions.assertDoesNotThrow(()-> userService.join("id", "password", "example@gmail.com", "name"));
    }
}
