package fastcampus.workschedulemanagementbackend.dto.response.useraccount;

import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;

public record UserAccountGetResponse(UserAccountDto user) {

    public static UserAccountGetResponse of(UserAccountDto userAccountDto) {
        return new UserAccountGetResponse(userAccountDto);
    }


}
