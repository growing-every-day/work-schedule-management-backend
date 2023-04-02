package fastcampus.workschedulemanagementbackend.domain.constants;

import lombok.Getter;

import java.util.Arrays;

public enum ScheduleType {
    DUTY("DUTY"),
    LEAVE("LEAVE");

    @Getter
    private final String label;

    ScheduleType(String label) {
        this.label = label;
    }

    public static ScheduleType valueOfLabel(String label){
        return Arrays.stream(values())
                .filter(value -> value.label.equals(label))
                .findAny()
                .orElse(null);
    }
}
