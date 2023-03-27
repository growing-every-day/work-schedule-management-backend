package fastcampus.workschedulemanagementbackend.dto.response.useraccount;

import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;

import java.util.List;

public record UserAccountGetAllUserResponse(List<UserAccountDto> users) {

    public static UserAccountGetAllUserResponse of(List<UserAccountDto> userAccountDtos) {
        return new UserAccountGetAllUserResponse(userAccountDtos);
    }
}
