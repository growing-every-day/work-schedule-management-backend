package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.dto.FieldErrorDto;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.dto.ValidationErrorDto;
import fastcampus.workschedulemanagementbackend.dto.request.useraccount.UserAccountJoinRequest;
import fastcampus.workschedulemanagementbackend.dto.request.useraccount.UserAccountUpdateRequest;
import fastcampus.workschedulemanagementbackend.dto.response.useraccount.*;
import fastcampus.workschedulemanagementbackend.common.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.common.error.ErrorCode;
import fastcampus.workschedulemanagementbackend.common.error.FieldValidationException;
import fastcampus.workschedulemanagementbackend.common.security.UserAccountPrincipal;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import fastcampus.workschedulemanagementbackend.common.utils.AESUtil;
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
    public ResponseEntity<UserAccountJoinResponse> join(@Valid @RequestBody UserAccountJoinRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FieldValidationException(ErrorCode.FIELD_VALIDATION_FAILED, handleBindingResult(bindingResult));
        }

        UserAccountDto user = userAccountService.join(request.toDto());

        return new ResponseEntity<>(UserAccountJoinResponse.of(true, user), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserAccountGetAllUserResponse> getAllUserAccounts(@RequestParam(required = false) String name) {

        List<UserAccountDto> userAccountDtoList = userAccountService.getAllUserAccounts(name != null ? aesUtil.encrypt(name) : null);
        return new ResponseEntity<>(UserAccountGetAllUserResponse.of(userAccountDtoList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountGetResponse> getUserAccount(@PathVariable Long id) {
        return userAccountService.getUserAccountById(id)
                .map(userAccountDto -> new ResponseEntity<>(UserAccountGetResponse.of(userAccountDto), HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_ACCOUNT_QUERY_FAILED));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<UserAccountUpdateResponse> updateUserAccount(@PathVariable Long id,
                                                                       @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal,
                                                                       @Valid @RequestBody UserAccountUpdateRequest userAccountUpdateRequest,
                                                                       BindingResult bindingResult) {

        checkAdmin(id, userAccountPrincipal);

        if (bindingResult.hasErrors()) {
            throw new FieldValidationException(ErrorCode.FIELD_VALIDATION_FAILED, handleBindingResult(bindingResult));
        }

        return userAccountService.updateUserAccount(id, userAccountUpdateRequest.toDto())
                .map(updatedUserAccount -> new ResponseEntity<>(UserAccountUpdateResponse.of(true, updatedUserAccount), HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_ACCOUNT_UPDATE_FAILED));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<UserAccountDeleteResponse> deleteUserAccount(@PathVariable Long id,
                                                                       @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal) {

        checkAdmin(id, userAccountPrincipal);

        return userAccountService.deleteUserAccount(id)
                .map(deletedUserAccount -> new ResponseEntity<>(UserAccountDeleteResponse.of(true), HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_ACCOUNT_DELETE_FAILED));
    }

    private static void checkAdmin(Long id, UserAccountPrincipal userAccountPrincipal) {
        boolean isAdmin = userAccountPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userAccountPrincipal.getId().equals(id)) {
            throw new BadRequestException(ErrorCode.NO_ADMIN);
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
