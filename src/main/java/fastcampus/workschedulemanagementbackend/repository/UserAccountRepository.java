package fastcampus.workschedulemanagementbackend.repository;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
<<<<<<< main
    Optional<UserAccount> findByUsername(String username);
=======
    Optional<UserAccount> findById(String id);
>>>>>>> feat #7- create and test basic UserAccountService
}