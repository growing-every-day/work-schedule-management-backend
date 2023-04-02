package fastcampus.workschedulemanagementbackend.dto.request.workschedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import java.time.LocalDate;
public record WorkScheduleRequest(
        @JsonProperty("event_id")
        String eventId,
        ScheduleType category,
        LocalDate start,
        LocalDate end
) {
    public WorkScheduleDto toDto(String createdByName){
        if (eventId == null){
            return WorkScheduleDto.of(null, category(), start, end, createdByName);
        }else
            return WorkScheduleDto.of(Long.valueOf(eventId), category(), start, end, createdByName);
    }

}
