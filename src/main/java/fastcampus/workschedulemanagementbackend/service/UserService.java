package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepository userAccountRepository;
    //TODO : implement
    public UserAccount join(String id, String password, String email, String name){
        //회원가입하려는 id로 회원가입된 userAccount가 있는지
        Optional<UserAccount> userAccount = userAccountRepository.findById(id);
        //회원가입 진행 = UserAccount를 등록
        userAccountRepository.save(UserAccount.of(id,password,email,name));
        return UserAccount.of(id,password,email,name);
    }
}
