package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableJpaAuditing
class UserAccountServiceTest {

    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserAccountService userAccountService;

    @DisplayName("check id,password without security")
    @Test
    public void check_id_pw() {
        UserAccount userAccount = UserAccount.of("lyh951212", "1234", "yeonhee", "@naver");
        userAccountRepository.save(userAccount);

        assertThat(userAccountService.isIdAndPasswordCorrect("lyh951212", "1234")).isTrue();
        assertThat(userAccountService.isIdAndPasswordCorrect("lyh951212", "12345")).isFalse();

    }
}