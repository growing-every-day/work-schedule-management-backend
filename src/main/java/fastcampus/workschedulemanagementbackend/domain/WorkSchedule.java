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
        @Index(columnList = "user_account_id"),
        @Index(columnList = "start_date"),
        @Index(columnList = "end_date")
})
@Entity
public class WorkSchedule extends BaseWriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Setter
//    @Column(nullable = false)
//    private String name; // 이름
//
//    @Setter
//    @Column(nullable = false)
//    private String email; // 이메일
    @Setter
    @Column(nullable = false, columnDefinition = "ENUM('DUTY','LEAVE')")
    @Enumerated(EnumType.STRING)
    private ScheduleType category; // 연차/당직 카테고리

    @Setter
    @Column(nullable = false, name = "start_date")
    private LocalDate start; // 시작일

    @Setter
    @Column(nullable = false, name = "end_date")
    private LocalDate end; // 종료일

    @Setter
    @ToString.Exclude
    @JoinColumn(name = "user_account_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount userAccount;
    protected WorkSchedule() {
    }

    private WorkSchedule(ScheduleType category, LocalDate startDate, LocalDate endDate) {
        this.category = category;
        this.start = startDate;
        this.end = endDate;
    }

    public static WorkSchedule of(ScheduleType category, LocalDate startDate, LocalDate endDate) {
        return new WorkSchedule(category, startDate, endDate);
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
