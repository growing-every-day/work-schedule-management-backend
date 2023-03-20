package fastcampus.workschedulemanagementbackend.domain;

import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Table(indexes = { // 빠르게 서칭이 가능하도록 인덱싱을 걸어준다.
        @Index(columnList = "id"),
        @Index(columnList = "name"),
        @Index(columnList = "email"),
})
@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Setter
    @Column(nullable = false)
    private String id; // 아이디

    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(columnDefinition = "ENUM('ADMIN','USER')")
    @Enumerated(EnumType.STRING)
    private UserRoleType role = UserRoleType.USER; // 사용자 역할

    @Setter
    @Column
    private int remainedVacationCount;

}
