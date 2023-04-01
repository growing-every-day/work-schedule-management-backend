package fastcampus.workschedulemanagementbackend.dto;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public static UserAccountDto of(String username, String password, String name, String email) {
        return new UserAccountDto(null, username, password, name, email, null, null, null, null);
    }

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

    public UserAccount toEntity() {
        return UserAccount.of(
                username,
                password,
                name,
                email
        );
    }

    public UserAccount toEntity(PasswordEncoder passwordEncoder, AESUtil aesUtil) {
        return UserAccount.of(
                username,
                passwordEncoder.encode(password),
                aesUtil.encrypt(name),
                aesUtil.encrypt(email)
        );
    }


}