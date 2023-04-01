package fastcampus.workschedulemanagementbackend.dto;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
    public static WorkScheduleDto from(WorkSchedule entity, AESUtil aesUtil){
        return WorkScheduleDto.of(
                entity.getEventId(),
                entity.getUserAccount().getId(),
                aesUtil.decrypt(entity.getUserAccount().getName()),
                aesUtil.decrypt(entity.getUserAccount().getEmail()),
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

    public static WorkScheduleDto insert(UserAccount entity, WorkScheduleDto workScheduleDto, AESUtil aesUtil) {
        return WorkScheduleDto.of(null,
                entity.getId(),
                aesUtil.decrypt(entity.getName()),
                aesUtil.decrypt(entity.getEmail()),
                workScheduleDto.category(),
                workScheduleDto.start(),
                workScheduleDto.end(),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public WorkSchedule toEntity(UserAccount userAccount, AESUtil aesUtil){
        return WorkSchedule.of(
                userAccount,
                category,
                start,
                end
        );
    }
}
