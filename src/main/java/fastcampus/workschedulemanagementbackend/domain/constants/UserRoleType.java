package fastcampus.workschedulemanagementbackend.domain.constants;

import lombok.Getter;

public enum UserRoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    @Getter private final String value;

    UserRoleType(String value) {
        this.value = value;
    }
}
