package fastcampus.workschedulemanagementbackend.domain.constants;

public enum UserRoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    UserRoleType(String value) {
        this.value = value;
    }
}
