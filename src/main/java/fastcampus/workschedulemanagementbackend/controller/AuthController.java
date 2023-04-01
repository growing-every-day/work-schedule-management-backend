package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.dto.*;
import fastcampus.workschedulemanagementbackend.error.FieldValidationException;
import fastcampus.workschedulemanagementbackend.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserAuthService userAuthService;

    /**
     * 로그인 시 access 토큰, refresh 토큰 모두 새로 만들어준다.
     *
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new FieldValidationException("입력한 값이 올바르지 않습니다.", handleBindingResult(bindingResult));
        }

        LoginResponseDto login = userAuthService.login(user);
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    /**
     * access토큰 내부의 유저 정보를 확인한 후 access, refresh 토큰 모두 새로 만들어준다.
     *
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
        return new ResponseEntity<>(userAuthService.refreshToken(token), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody TokenDto token) {
        return new ResponseEntity(userAuthService.logout(token), HttpStatus.OK);
    }

    private ValidationErrorDto handleBindingResult(BindingResult bindingResult) {

        return ValidationErrorDto
                .of(
                        bindingResult.
                                getFieldErrors().stream()
                                .map(error -> FieldErrorDto.of(error.getField(), error.getDefaultMessage()))
                                .collect(Collectors.toList()));
    }
}