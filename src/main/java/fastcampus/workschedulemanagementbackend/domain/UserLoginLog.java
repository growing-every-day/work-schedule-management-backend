package fastcampus.workschedulemanagementbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "user_account_id"),
        @Index(columnList = "userAccountUserName")
})
@Entity
public class UserLoginLog  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_account_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount userAccount;

    @Column(nullable = false)
<<<<<<< main
    private String userAccountUsername;
=======
    private String userAccountUserName;
>>>>>>> #14 - 엔티티 클래스 primary key 변수 네임 변경: seq -> id

    @Column(nullable = false)
    private String userAgent;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime loginTime;

    protected UserLoginLog() {
    }

    private UserLoginLog(UserAccount userAccount, String userAgent, String ip) {
        this.userAccount = userAccount;
<<<<<<< main
        this.userAccountUsername = userAccount.getUsername();
=======
        this.userAccountUserName = userAccount.getUserName();
>>>>>>> #14 - 엔티티 클래스 primary key 변수 네임 변경: seq -> id
        this.userAgent = userAgent;
        this.ip = ip;
        this.loginTime = LocalDateTime.now();
    }

    public static UserLoginLog of(UserAccount userAccount, String userAgent, String ip) {
        return new UserLoginLog(userAccount, userAgent, ip);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLoginLog that)) return false;
        return this.getId() != null & this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
