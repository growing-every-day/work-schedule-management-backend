package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import fastcampus.workschedulemanagementbackend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    @GetMapping()
    public ResponseEntity<List<WorkScheduleDto>> getAllSchedules(
            @RequestParam(required = false, value = "userid") Long id,
            @RequestParam(value = "year") String year,
            @RequestParam(value = "month") String month) throws ParseException {
        System.out.println("ScheduleController.getAllSchedules");
        List<WorkScheduleDto> scheduleDtoList = scheduleService.getAllSchedules(id, year, month);
        return new ResponseEntity<>(scheduleDtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/{userid}/create")
    public ResponseEntity<WorkScheduleDto> createWorkSchedule(@RequestBody WorkSchedule workSchedule, @PathVariable String userid){
        workSchedule.setUserAccount();
    }
}
