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
    private String password; // 패스워드

    @Setter
    @Column(nullable = false)
    private String name; // 이름

    @Setter
    @Column(nullable = false)
    private String email; // 이메일

    @Setter
    @Column(columnDefinition = "ENUM('ADMIN','USER')")
    @Enumerated(EnumType.STRING)
    private UserRoleType role = UserRoleType.USER; // 권한

    @Setter
    @Column
    private int remainedVacationCount; // 연차 잔여 일수

}
