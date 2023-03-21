package fastcampus.workschedulemanagementbackend.domain;

import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserAccountTest TEST")
@EnableJpaAuditing
@DataJpaTest
class UserAccountTest {

    @Autowired UserAccountRepository userAccountRepository;

    @Test
    @DisplayName("BaseTimeEntity_테스트")
    void jpa_auditing_field_test() {
        //given
        LocalDateTime now = LocalDateTime.of(2023,3,21,0,0,0);

        UserAccount userAccount = UserAccount.of("lyh951212", "1234", "yeonhee", "@naver");
        userAccountRepository.save(userAccount);

        // when
        List<UserAccount> findUserList = userAccountRepository.findAll();

        // then
        UserAccount findUser = findUserList.get(0);

        System.out.println("find user created time = " + findUser.getCreatedDate());
        Assertions.assertThat(findUser.getCreatedDate()).isAfter(now);
    }

}