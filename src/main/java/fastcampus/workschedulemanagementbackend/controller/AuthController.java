package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.dto.LoginResponseDto;
import fastcampus.workschedulemanagementbackend.dto.LoginRequestDto;
import fastcampus.workschedulemanagementbackend.dto.TokenDto;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@EnableJpaAuditing
public class AuthController {
    private final UserAccountService userAccountService;

    @PostMapping("/test")
    public String test() {

        return "<h1>test 통과</h1>";
    }

    @PostMapping("/admin")
    public String admin() {

        return "<h1>admin 통과</h1>";
    }

    /**
     * 로그인 시 access 토큰, refresh 토큰 모두 새로 만들어준다.
     * @param user
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto user, HttpServletResponse response) throws Exception {
        System.out.println("AuthController.login");
        return new ResponseEntity<>(userAccountService.login(user), HttpStatus.OK);
    }

    /**
     * access토큰 내부의 유저 정보를 확인한 후 access, refresh 토큰 모두 새로 만들어준다.
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
        return new ResponseEntity<>( userAccountService.refreshToken(token), HttpStatus.OK);
    }
}