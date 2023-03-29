package fastcampus.workschedulemanagementbackend.dto.request.useraccount;

import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record UserAccountUpdateRequest(
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
                message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
        String password,
        @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 2~5자 한글로 입력하세요.")
        String name,
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @Pattern(regexp = "(?i)^(USER|ADMIN)$", message = "유저 권한은 USER 또는 ADMIN 중 하나여야 합니다.")
        String role,
        @PositiveOrZero(message = "잔여 휴가는 0 이상의 숫자여야 합니다.")
        Integer remainedVacationCount
) {

    public UserAccountDto toDto() {
        UserRoleType userRole = role != null ? UserRoleType.valueOf(role.toUpperCase()) : null;
        return UserAccountDto.of(null, null, password, name, email, userRole, remainedVacationCount);
    }
}