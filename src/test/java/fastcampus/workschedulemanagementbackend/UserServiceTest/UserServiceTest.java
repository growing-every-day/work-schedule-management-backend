package fastcampus.workschedulemanagementbackend.UserServiceTest;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.exception.wsAppException;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserAccountRepository userAccountRepository;

    @Test
    void 회원가입이_정상적으로_동작하는_경우(){
        String id = "id";
        String password = "password";
        String email = "example@gmail.com";
        String name = "name";

        //mocking
        when(userAccountRepository.findById(id)).thenReturn(Optional.empty());
        when(userAccountRepository.save(any())).thenReturn(Optional.of(mock(UserAccount.class)));

        Assertions.assertDoesNotThrow(()-> userService.join(id, password, email, name));
    }

    @Test
    void 회원가입시_id로_회원가입한_유저가_이미_있는경우(){
        String id = "id";
        String password = "password";
        String email = "example@gmail.com";
        String name = "name";

        //mocking
        when(userAccountRepository.findById(id)).thenReturn(Optional.of(mock(UserAccount.class)));
        when(userAccountRepository.save(any())).thenReturn(Optional.of(mock(UserAccount.class)));

        Assertions.assertThrows(ClassCastException.class, () -> userService.join(id, password, email, name));
    }
}
