package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;


    public List<WorkScheduleDto> getAllSchedules(Long id, String year, String month) throws ParseException {
        if (month.length() == 1 ){
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
        if (schedules.isEmpty()){
            throw new BadRequestException("회원이 존재하지 않습니다.");
        }

        return schedules
                .stream()
                .map(WorkScheduleDto::from)
                .collect(Collectors.toList());
    }
}
