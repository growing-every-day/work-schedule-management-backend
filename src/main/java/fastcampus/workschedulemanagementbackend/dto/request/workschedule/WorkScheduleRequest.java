package fastcampus.workschedulemanagementbackend.dto.request.workschedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;
import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
public record WorkScheduleRequest(
        @JsonProperty("event_id")
        String eventId,
        @Pattern(regexp = "(?i)^(LEAVE|DUTY)$", message = "스케줄 카테고리는 LEAVE 또는 DUTY 중 하나여야 합니다.")
        String category,
        @FutureOrPresent(message = "시작일은 현재 또는 미래여야 합니다.")
        LocalDate start,
        @FutureOrPresent(message = "종료일은 현재 또는 미래여야 합니다.")
        LocalDate end
) {
    public WorkScheduleRequest(
            @JsonProperty("event_id")
            String eventId,
            @Pattern(regexp = "(?i)^(LEAVE|DUTY)$", message = "스케줄 카테고리는 LEAVE 또는 DUTY 중 하나여야 합니다.")
            String category,
            @FutureOrPresent(message = "시작일은 현재 또는 미래여야 합니다.")
            LocalDate start,
            @FutureOrPresent(message = "종료일은 현재 또는 미래여야 합니다.")
            LocalDate end
    ) {
        this.eventId = eventId;
        this.category = category != null ? category.toUpperCase() : null;
        this.start = start;
        this.end = end;
    }

    public WorkScheduleDto toDto(String createdByName){
        if (eventId == null){
            return WorkScheduleDto.of(null, ScheduleType.valueOf(category), start, end, createdByName);
        } else {
            return WorkScheduleDto.of(Long.valueOf(eventId), ScheduleType.valueOf(category), start, end, createdByName);
        }
    }
}