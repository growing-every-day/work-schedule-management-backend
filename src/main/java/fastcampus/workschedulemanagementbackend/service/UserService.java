package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    //TODO : implement
    public UserAccount join(String id, String password, String email, String name){
        return UserAccount.of("id", "password", "example@gmail.com", "name");
    }
}
