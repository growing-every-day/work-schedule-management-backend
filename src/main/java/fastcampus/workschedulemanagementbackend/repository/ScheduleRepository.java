package fastcampus.workschedulemanagementbackend.repository;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    @Query(value = "select DISTINCT c from WorkSchedule c left join fetch UserAccount")
    List<WorkSchedule> findAllWithUserAccount();
}
