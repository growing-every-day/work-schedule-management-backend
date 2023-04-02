package fastcampus.workschedulemanagementbackend.dto.response.useraccount;

import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;

public record UserAccountJoinResponse(Boolean created, UserAccountDto createdUser) {

    public static UserAccountJoinResponse of(Boolean created, UserAccountDto createdUser) {
        return new UserAccountJoinResponse(created, createdUser);
    }

}