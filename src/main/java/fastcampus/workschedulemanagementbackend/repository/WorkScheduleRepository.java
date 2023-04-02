package fastcampus.workschedulemanagementbackend.repository;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    @Query(value = "select DISTINCT c from WorkSchedule c left join fetch c.userAccount")
    //@Query(value = "select s, u.name, u.email as email from WorkSchedule s left outer join UserAccount u on s.userAccountId = u.id")
    List<WorkSchedule> findAllWithUserAccount();

    @Query(value = "select distinct s from WorkSchedule s left join fetch s.userAccount where (function('date_format', s.start, '%Y, %m') = function('date_format', :date, '%Y, %m') or function('date_format', s.end, '%Y, %m') = function('date_format', :date, '%Y, %m')) and s.userAccount.id = :id")
    List<WorkSchedule> findAllByUserAccountId(@Param("id") Long id, @Param("date") Date date);


}
