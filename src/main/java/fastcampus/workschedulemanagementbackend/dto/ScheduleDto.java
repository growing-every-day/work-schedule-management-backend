package fastcampus.workschedulemanagementbackend.dto;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import lombok.AllArgsConstructor;

import java.util.Date;

public record ScheduleDto(
        Long event_id,
        Long user_account_id,
        String category,
        Date start_date,
        Date end_date,
        String created_by,
        Long created_by_id,
        Date created_at,
        String modified_by,
        Long modified_by_id,
        Date modified_at
) {
//    public static ScheduleDto entityToDto(WorkSchedule entity){
//        return ScheduleDto();
//    }
}
