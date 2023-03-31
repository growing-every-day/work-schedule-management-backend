package fastcampus.workschedulemanagementbackend.dto;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record WorkScheduleDto(
        Long eventId,
        Long userAccountId,
        String name,
        String email,
        ScheduleType category,
        LocalDate start,
        LocalDate end,
        String createdBy,
        Long createdById,
        LocalDateTime createdAt,
        String modifiedBy,
        Long modifiedById,
        LocalDateTime modifiedAt
) {
    public static WorkScheduleDto of(Long eventId,
                                     Long userAccountId,
                                     String name,
                                     String email,
                                     ScheduleType category,
                                     LocalDate start,
                                     LocalDate end,
                                     String createdBy,
                                     Long createdById,
                                     LocalDateTime createdAt,
                                     String modifiedBy,
                                     Long modifiedById,
                                     LocalDateTime modifiedAt){
        return new WorkScheduleDto(eventId, userAccountId, name, email, category, start, end, createdBy, createdById, createdAt, modifiedBy, modifiedById, modifiedAt);
    }
    public static WorkScheduleDto from(WorkSchedule entity){
        return WorkScheduleDto.of(
                entity.getEventId(),
                entity.getUserAccount().getId(),
                entity.getUserAccount().getName(),
                entity.getUserAccount().getEmail(),
                entity.getCategory(),
                entity.getStart(),
                entity.getEnd(),
                entity.getCreatedBy(),
                entity.getCreatedById(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedById(),
                entity.getModifiedAt()
        );
    }
}
