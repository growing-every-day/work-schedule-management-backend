package fastcampus.workschedulemanagementbackend.domain.constants;

import lombok.Getter;

public enum ScheduleType {
    DUTY("DUTY"),
    LEAVE("LEAVE");

    @Getter
    private final String value;

    ScheduleType(String value) {
        this.value = value;
    }
}
