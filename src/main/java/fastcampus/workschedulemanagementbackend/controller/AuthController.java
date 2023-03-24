package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.config.JwtTokenProvider;
import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.dto.UserLoginDto;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@EnableJpaAuditing
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAccountRepository userAccountRepository;

    @PostMapping("/test")
    public String test(){

        return "<h1>test 통과</h1>";
    }

    @PostMapping("/join")
    public String join(){
        System.out.println("UseController.join");
        UserAccount user = UserAccount.of("lyh951212", "1234", "yeon", "naver");

        userAccountRepository.save(user);

        return user.toString();

    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto user) {
        System.out.println("user.toString() = " + user.toString());
        UserAccount member = userAccountRepository.findByUserName(user.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        System.out.println("member.toString() = " + member.toString());
        return jwtTokenProvider.createToken(member.getUsername(), member.getPassword());
    }

}