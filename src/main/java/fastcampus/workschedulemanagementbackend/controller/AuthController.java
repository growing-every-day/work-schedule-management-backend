package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.dto.LoginRequestDto;
import fastcampus.workschedulemanagementbackend.dto.LoginResponseDto;
import fastcampus.workschedulemanagementbackend.dto.TokenDto;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserAccountService userAccountService;

    /**
     * 로그인 시 access 토큰, refresh 토큰 모두 새로 만들어준다.
     *
     * @param user
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto user, HttpServletResponse response) throws Exception {
        return new ResponseEntity<>(userAccountService.login(user), HttpStatus.OK);
    }

    /**
     * access토큰 내부의 유저 정보를 확인한 후 access, refresh 토큰 모두 새로 만들어준다.
     *
     * @param accessToken 엑세스 토큰
     * @return
     * @throws Exception
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody Map<String, String> accessToken) throws Exception {
        return new ResponseEntity<>(userAccountService.refreshToken(accessToken.get("accessToken")), HttpStatus.OK);
    }

    /**
     * accessToken에 담긴 유저정보를 꺼내서 refresh token을 지워준다.
     * @param accessToken 엑세스 토큰
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody Map<String, String> accessToken) {
        return new ResponseEntity(userAccountService.logout(accessToken.get("accessToken")), HttpStatus.OK);
    }
}