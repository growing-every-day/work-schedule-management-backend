package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.repository.ScheduleRepository;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserAccountRepository userAccountRepository;

    private final AESUtil aesUtil;

    @Transactional
    public List<WorkScheduleDto> getAllSchedules(Long id, String year, String month) throws ParseException {
        if (month.length() == 1) {
            month = "0" + month;
        }
        System.out.println("id = " + id);
        String dateStr = year + "/" + month + "/01";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = formatter.parse(dateStr);
        List<WorkSchedule> schedules = id != null ?
                scheduleRepository.findAllByUserAccountId(id, date)
                :
                scheduleRepository.findAllWithUserAccount();
        if (schedules.isEmpty()) {
            throw new BadRequestException("스케줄 정보가 존재하지 않습니다.");
        }

        return schedules
                .stream()
                .map(schedule -> WorkScheduleDto.from(schedule, aesUtil))
                .collect(Collectors.toList());
    }
    @Transactional
    public WorkScheduleDto createWorkSchedule(WorkScheduleDto workScheduleDto, Long id) {
        return Optional.ofNullable(userAccountRepository.findById(id)
                        .map(user -> workScheduleDto.toEntity(user, aesUtil))//DB와 통신할 Entity형태로 변환
                        .map(workSchedule -> WorkScheduleDto.from(scheduleRepository.save(workSchedule), aesUtil))
                        .get())
                .orElseThrow(() -> new BadRequestException(String.format("회원 번호(%d)를 찾을 수 없습니다", id)));
    }

    @Transactional
    public Boolean deleteWorkSchedule(Long eventId){
        if (Optional.ofNullable(scheduleRepository.findById(eventId)).isEmpty()){
            throw new BadRequestException(String.format("스케쥴 정보(%d)를 찾을 수 없습니다.", eventId));
        }else{
            scheduleRepository.deleteById(eventId);
            return true;
        }
    }
}
