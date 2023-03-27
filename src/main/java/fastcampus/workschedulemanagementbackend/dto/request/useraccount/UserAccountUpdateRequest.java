package fastcampus.workschedulemanagementbackend.dto.request.useraccount;

import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;

public record UserAccountUpdateRequest(
        String password,
        String name,
        String email,
        String role,
        Integer remainedVacationCount
) {
    public static UserAccountUpdateRequest of(String password, String name, String email, String role, Integer remainedVacationCount) {
        return new UserAccountUpdateRequest(password, name, email, role, remainedVacationCount);
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(null, null, password, name, email, UserRoleType.valueOf(role.toUpperCase()), remainedVacationCount);
    }
}