package fastcampus.workschedulemanagementbackend.dto.response.useraccount;

import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;

public record UserAccountUpdateResponse(Boolean modified, UserAccountDto updatedUser) {

    public static UserAccountUpdateResponse of(Boolean modified, UserAccountDto updatedUser) {
        return new UserAccountUpdateResponse(modified, updatedUser);
    }

}
