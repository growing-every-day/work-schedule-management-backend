package fastcampus.workschedulemanagementbackend.domain;

import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "category"),
        @Index(columnList = "user_account_id")
})
@Entity
public class WorkSchedule extends BaseWriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "user_account_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount userAccount;

    @Setter
    @Column(nullable = false, columnDefinition = "ENUM('DUTY','LEAVE')")
    @Enumerated(EnumType.STRING)
    private ScheduleType category; // 연차/당직 카테고리

    @Setter
    @Column(nullable = false)
    private LocalDate startDate; // 시작일

    @Setter
    @Column(nullable = false)
    private LocalDate endDate; // 종료일

    protected WorkSchedule() {
    }

    private WorkSchedule(UserAccount userAccount, ScheduleType category, LocalDate startDate, LocalDate endDate) {
        this.userAccount = userAccount;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static WorkSchedule of(UserAccount userAccount, ScheduleType category, LocalDate startDate, LocalDate endDate) {
        return new WorkSchedule(userAccount, category, startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkSchedule that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
