package fastcampus.workschedulemanagementbackend.domain;

import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(
        indexes = { // 빠르게 서칭이 가능하도록 인덱싱을 걸어준다.
        @Index(columnList = "username"),
        @Index(columnList = "name"),
        @Index(columnList = "email"),
})
@Entity
public class UserAccount extends BaseTimeEntity {

    @Transient
    private final int DEFAULT_VACATION_DAYS = 25; // 기본 연차 일수 - 25일

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인 아이디

    @Column(nullable = false)
    @Setter
    private String password; // 패스워드

    @Setter
    @Column(nullable = false)
    private String name; // 이름

    @Setter
    @Column(nullable = false)
    private String email; // 이메일

    @Setter
    @Column(columnDefinition = "ENUM('ADMIN','USER')")
    @Enumerated(EnumType.STRING)
    private UserRoleType role = UserRoleType.USER; // 권한

    @Setter
    @Column
    private Integer remainedVacationCount; // 연차 잔여 일수

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private final Set<WorkSchedule> workSchedules = new LinkedHashSet<>();


    @ToString.Exclude
    @OrderBy("loginTime DESC")
    @OneToMany(mappedBy = "userAccount")
    private final Set<UserLoginLog> userLoginLogs = new LinkedHashSet<>();

    protected UserAccount() {
    }

    private UserAccount(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.remainedVacationCount = DEFAULT_VACATION_DAYS;
    }

    public static UserAccount of(String username, String password, String name, String email) {
        return new UserAccount(username, password, name, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public void update(UserAccountDto userAccountDto) {

        if (userAccountDto.password() != null) {
            this.password = userAccountDto.password();
        }
        if (userAccountDto.name() != null) {
            this.name = userAccountDto.name();
        }
        if (userAccountDto.email() != null) {
            this.email = userAccountDto.email();
        }
        if (userAccountDto.role() != null) {
            this.role = userAccountDto.role();
        }
        if (userAccountDto.remainedVacationCount() != null) {
            this.remainedVacationCount = userAccountDto.remainedVacationCount();
        }
    }
}
