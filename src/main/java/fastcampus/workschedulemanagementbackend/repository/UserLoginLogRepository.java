package fastcampus.workschedulemanagementbackend.repository;

import fastcampus.workschedulemanagementbackend.domain.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}
