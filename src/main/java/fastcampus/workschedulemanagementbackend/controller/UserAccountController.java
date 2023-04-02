package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.dto.FieldErrorDto;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.dto.ValidationErrorDto;
import fastcampus.workschedulemanagementbackend.dto.request.useraccount.UserAccountJoinRequest;
import fastcampus.workschedulemanagementbackend.dto.request.useraccount.UserAccountUpdateRequest;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.*;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.error.FieldValidationException;
import fastcampus.workschedulemanagementbackend.security.UserAccountPrincipal;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final AESUtil aesUtil;

    @PostMapping("/signup")
    public Response<UserAccountJoinResponse> join(@Valid @RequestBody UserAccountJoinRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FieldValidationException("입력한 값이 올바르지 않습니다.", handleBindingResult(bindingResult));
        }

        UserAccountDto user = userAccountService.join(request.toDto());
        return Response.success(UserAccountJoinResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<UserAccountGetAllUserResponse> getAllUserAccounts(@RequestParam(required = false) String name) {

        List<UserAccountDto> userAccountDtoList = userAccountService.getAllUserAccounts(name != null ? aesUtil.encrypt(name) : null);
        return new ResponseEntity<>(new UserAccountGetAllUserResponse(userAccountDtoList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountGetResponse> getUserAccount(@PathVariable Long id) {
        return userAccountService.getUserAccountById(id)
                .map(userAccountDto -> new ResponseEntity<>(UserAccountGetResponse.of(userAccountDto), HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("회원 조회에 실패했습니다."));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<UserAccountUpdateResponse> updateUserAccount(@PathVariable Long id,
                                                                       @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal,
                                                                       @Valid @RequestBody UserAccountUpdateRequest userAccountUpdateRequest,
                                                                       BindingResult bindingResult) {

        checkAdmin(id, userAccountPrincipal, "다른 회원을 수정할 권한이 없습니다.");

        if (bindingResult.hasErrors()) {
            throw new FieldValidationException("입력한 값이 올바르지 않습니다.", handleBindingResult(bindingResult));
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
