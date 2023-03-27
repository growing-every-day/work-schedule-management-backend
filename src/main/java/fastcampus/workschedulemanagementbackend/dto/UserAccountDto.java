package fastcampus.workschedulemanagementbackend.dto;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;

import java.time.LocalDateTime;

public record UserAccountDto(
        Long id,
        String username,
        String password,
        String name,
        String email,
        UserRoleType role,
        Integer remainedVacationCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static UserAccountDto of(Long id, String username, String password, String name, String email, UserRoleType role, Integer remainedVacationCount) {
        return new UserAccountDto(id, username, password, name, email, role, remainedVacationCount, null, null);
    }

    public static UserAccountDto of(Long id, String username, String password, String name, String email, UserRoleType role, Integer remainedVacationCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new UserAccountDto(id, username, password, name, email, role, remainedVacationCount, createdAt, modifiedAt);
    }

    public static UserAccountDto of(Long id, String username, String name, String email, UserRoleType role, Integer remainedVacationCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new UserAccountDto(id, username, null, name, email, role, remainedVacationCount, createdAt, modifiedAt);
    }

    public static UserAccountDto from(UserAccount entity) {
        return UserAccountDto.of(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getRemainedVacationCount(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public static UserAccountDto fromWithoutPassword(UserAccount entity) {
        return UserAccountDto.of(
                entity.getId(),
                entity.getUsername(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getRemainedVacationCount(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}