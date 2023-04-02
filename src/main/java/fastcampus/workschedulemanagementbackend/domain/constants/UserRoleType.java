package fastcampus.workschedulemanagementbackend.domain.constants;

import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import lombok.Getter;

public enum UserRoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    @Getter private final String value;

    UserRoleType(String value) {
        this.value = value;
    }

    public static UserRoleType of(String value) {
        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.getValue().equals(value)) {

                return userRoleType;
            }
        }
        throw new BadRequestException("해당하는 유저 권한이 없습니다.");
    }
}
