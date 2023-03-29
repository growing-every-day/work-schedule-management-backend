package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.dto.request.useraccount.UserAccountJoinRequest;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.Response;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.UserAccountJoinResponse;
import fastcampus.workschedulemanagementbackend.dto.FieldErrorDto;
import fastcampus.workschedulemanagementbackend.dto.LoginDto;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.dto.ValidationErrorDto;
import fastcampus.workschedulemanagementbackend.dto.request.useraccount.UserAccountUpdateRequest;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.UserAccountDeleteResponse;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.UserAccountGetAllUserResponse;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.UserAccountGetResponse;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.UserAccountUpdateResponse;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.error.FieldValidationException;
import fastcampus.workschedulemanagementbackend.security.UserAccountPrincipal;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager; // TODO: 테스트 위해 임시로 추가함. 추후 삭제할 것.

    @PostMapping("/signup")
    public Response<UserAccountJoinResponse> join(@RequestBody UserAccountJoinRequest request){
        //join
        log.error("User {} is trying to join.", request.getUsername());
        UserAccountDto user = userAccountService.join(request.toDto());
        log.error("User {} joined successfully.", request.getUsername());
        return Response.success(UserAccountJoinResponse.fromWithoutUser(user));
    }

    // TODO: 테스트 위해 임시로 추가함. 추후 삭제할 것.
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto, HttpServletRequest req) throws ServletException {
        log.debug("User {} is trying to log in.", loginDto.getUsername());
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication auth = authenticationManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        return new ResponseEntity<>("User signed-in successfully!. ", HttpStatus.OK);
    }

    // TODO: 테스트 위해 임시로 추가함. 추후 삭제할 것.
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.logout();
        return new ResponseEntity<>("User signed-out successfully!.", HttpStatus.OK);
    }

    // TODO: 테스트 위해 임시로 추가함. 추후 삭제할 것.
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal) {
        return new ResponseEntity<>(userAccountPrincipal.getUsername(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserAccountGetAllUserResponse> getAllUserAccounts(@RequestParam(required = false) String name) {
        List<UserAccountDto> userAccountDtoList = userAccountService.getAllUserAccounts(name);
        return new ResponseEntity<>(new UserAccountGetAllUserResponse(userAccountDtoList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountGetResponse> getUserAccount(@PathVariable Long id) {
        return userAccountService.getUserAccountById(id)
                .map(userAccountDto -> new ResponseEntity<>(new UserAccountGetResponse(userAccountDto), HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("회원 조회에 실패했습니다."));
    }

    // TODO: 추후에 암호 인코딩 필요
    @PostMapping("/{id}/update")
    public ResponseEntity<UserAccountUpdateResponse> updateUserAccount(@PathVariable Long id,
                                                                       @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal,
                                                                       @Valid @RequestBody UserAccountUpdateRequest userAccountUpdateRequest,
                                                                       BindingResult bindingResult) {

        checkAdmin(id, userAccountPrincipal, "다른 회원을 수정할 권한이 없습니다.");

        if (bindingResult.hasErrors()) {
            throw new FieldValidationException("회원 정보 수정에 실패했습니다.", handleBindingResult(bindingResult));
        }

        return userAccountService.updateUserAccount(id, userAccountUpdateRequest.toDto())
                .map(updatedUserAccount -> new ResponseEntity<>(new UserAccountUpdateResponse(true, updatedUserAccount), HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("회원 정보 수정에 실패했습니다."));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<UserAccountDeleteResponse> deleteUserAccount(@PathVariable Long id,
                                                                       @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal) {

        checkAdmin(id, userAccountPrincipal, "다른 회원을 삭제할 권한이 없습니다.");

        return userAccountService.deleteUserAccount(id)
                .map(deletedUserAccount -> new ResponseEntity<>(new UserAccountDeleteResponse(true), HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("회원 삭제에 실패했습니다."));
    }

    private static void checkAdmin(Long id, UserAccountPrincipal userAccountPrincipal, String errorMessage) {
        boolean isAdmin = userAccountPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userAccountPrincipal.getId().equals(id)) {
            throw new BadRequestException(errorMessage);
        }
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
