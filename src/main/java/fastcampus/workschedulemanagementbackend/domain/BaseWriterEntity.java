package fastcampus.workschedulemanagementbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseWriterEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createdBy; // 생성자Id

    @LastModifiedBy
    @Column(nullable = false)
    private Long modifiedBy; // 수정자Id
}


