package fastcampus.workschedulemanagementbackend.repository;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
<<<<<<< main
<<<<<<< main
<<<<<<< main
    Optional<UserAccount> findByUsername(String username);
=======
    Optional<UserAccount> findById(String id);
>>>>>>> feat #7- create and test basic UserAccountService
=======
    Optional<UserAccount> findById(String id);
>>>>>>> feat #7- create and test basic UserAccountService
=======
    Optional<UserAccount> findByUserName(String userName);
<<<<<<< main
>>>>>>> feat #7- apply spring security + jwt
=======
    @Transactional
    @Modifying // select 문이 아님을 나타낸다
    @Query("update UserAccount u set u.refreshToken = ?1 where u.id = ?2")
    void updateRefreshToken(String refreshToken, Long id);
>>>>>>> feat #7 - spring security + jwt 기능 추가
}