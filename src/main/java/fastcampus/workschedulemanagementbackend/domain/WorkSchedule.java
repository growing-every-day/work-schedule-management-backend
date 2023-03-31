package fastcampus.workschedulemanagementbackend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Column(name = "id")
    private Long eventId;

    @Setter
    @Column(nullable = false, columnDefinition = "ENUM('DUTY','LEAVE')")
    @Enumerated(EnumType.STRING)
    private ScheduleType category; // 연차/당직 카테고리

    @Setter
    @Column(nullable = false, name = "start_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd", timezone="Asia/Seoul")
    private LocalDate start; // 시작일

    @Setter
    @Column(nullable = false, name = "end_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd", timezone="Asia/Seoul")
    private LocalDate end; // 종료일

    @Setter
    @JoinColumn(name = "user_account_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    //@JsonBackReference
    private UserAccount userAccount;
    protected WorkSchedule() {
    }

    private WorkSchedule(UserAccount userAccount, ScheduleType category, LocalDate startDate, LocalDate endDate) {
        this.userAccount = userAccount;
        this.category = category;
        this.start = startDate;
        this.end = endDate;
    }

    public static WorkSchedule of(UserAccount userAccount, ScheduleType category, LocalDate startDate, LocalDate endDate) {
        return new WorkSchedule(userAccount, category, startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkSchedule that)) return false;
        return this.getEventId() != null && this.getEventId().equals(that.getEventId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEventId());
    }
}
