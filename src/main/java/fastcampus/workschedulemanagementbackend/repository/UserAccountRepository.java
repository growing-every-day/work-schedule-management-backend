package fastcampus.workschedulemanagementbackend.repository;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
}