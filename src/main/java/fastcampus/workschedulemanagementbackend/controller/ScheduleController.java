package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.dto.FieldErrorDto;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.dto.ValidationErrorDto;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import fastcampus.workschedulemanagementbackend.error.FieldValidationException;
import fastcampus.workschedulemanagementbackend.service.ScheduleService;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserAccountService userAccountService;

    private final AESUtil aesUtil;
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
    public ResponseEntity<WorkScheduleDto> createWorkSchedule(@Valid @RequestBody WorkScheduleDto workScheduleDto,
                                                              @PathVariable Long userid,
                                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new FieldValidationException("스케쥴 정보를 받아오는데 실패했습니다.", handleBindingResult(bindingResult));
        }

        return new ResponseEntity<>(scheduleService.createWorkSchedule(workScheduleDto, userid), HttpStatus.OK);
    }

    @PostMapping(value = "/{userid}/update")
    public ResponseEntity<WorkScheduleDto> updateWorkSchedule(@Valid @RequestBody WorkScheduleDto workScheduleDto,
                                                              @PathVariable Long userid,
                                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new FieldValidationException("스케쥴 정보를 받아오는데 실패했습니다.", handleBindingResult(bindingResult));
        }

        return new ResponseEntity<>(scheduleService.createWorkSchedule(workScheduleDto, userid), HttpStatus.OK);
    }
    @PostMapping(value = "/{eventid}/delete")
    public Boolean updateWorkSchedule(@PathVariable Long eventId){
        return scheduleService.deleteWorkSchedule(eventId);
    }
    private ValidationErrorDto handleBindingResult(BindingResult bindingResult) {

        return ValidationErrorDto
                .of(
                        bindingResult.
                                getFieldErrors().stream()
                                .map(error -> FieldErrorDto.of(error.getField(), error.getDefaultMessage()))
                                .collect(Collectors.toList()));
    }
}
