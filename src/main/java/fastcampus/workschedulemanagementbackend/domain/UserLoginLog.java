package fastcampus.workschedulemanagementbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "user_account_seq"),
        @Index(columnList = "userAccountId")
})
@Entity
public class UserLoginLog  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "user_account_seq")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount userAccount;

    @Column(nullable = false)
    private String userAccountId;

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
        this.userAccountId = userAccount.getId();
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
        return this.getSeq() != null & this.getSeq().equals(that.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSeq());
    }
}
