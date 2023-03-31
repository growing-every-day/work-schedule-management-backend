package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.dto.ScheduleDto;
import fastcampus.workschedulemanagementbackend.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;


    public List<WorkSchedule> getAllSchedules(Long id, String year, String month) {
        List<WorkSchedule> schedules = scheduleRepository.findAllWithUserAccount();

        return schedules;
    }
}
