package fastcampus.workschedulemanagementbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseWriterEntity extends BaseTimeEntity{
    

    @Column(nullable = false, updatable = false)
    private String createdBy; // 생성자

    @CreatedBy
    @Column(nullable = true)
    private Long createdById;

    @Column(nullable = true)
    private String modifiedBy;

    @LastModifiedBy
    @Column(nullable = true)
    private Long modifiedById; // 수정자


}
