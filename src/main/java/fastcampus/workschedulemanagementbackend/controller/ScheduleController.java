package fastcampus.workschedulemanagementbackend.controller;

import fastcampus.workschedulemanagementbackend.controller.validator.WorkScheduleValidator;
import fastcampus.workschedulemanagementbackend.dto.FieldErrorDto;
import fastcampus.workschedulemanagementbackend.dto.ValidationErrorDto;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import fastcampus.workschedulemanagementbackend.dto.request.workschedule.WorkScheduleRequest;
import fastcampus.workschedulemanagementbackend.common.error.exception.BadRequestException;
import fastcampus.workschedulemanagementbackend.common.error.ErrorCode;
import fastcampus.workschedulemanagementbackend.common.error.exception.FieldValidationException;
import fastcampus.workschedulemanagementbackend.common.security.UserAccountPrincipal;
import fastcampus.workschedulemanagementbackend.service.WorkScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final WorkScheduleService scheduleService;

    @InitBinder("workScheduleRequest")
    void initBinder(WebDataBinder binder) {
        binder.addValidators(new WorkScheduleValidator());
    }

    @GetMapping()
    public ResponseEntity<List<WorkScheduleDto>> getAllSchedules(
            @RequestParam(required = false, value = "userid") Long id,
            @RequestParam(value = "year") String year,
            @RequestParam(value = "month") String month) throws ParseException {
        List<WorkScheduleDto> scheduleDtoList = scheduleService.getAllSchedules(id, year, month);
        return new ResponseEntity<>(scheduleDtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/{userid}/create")
    public ResponseEntity<WorkScheduleDto> createWorkSchedule(@Valid @RequestBody WorkScheduleRequest workScheduleRequest,
                                                              BindingResult bindingResult,
                                                              @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal,
                                                              @PathVariable Long userid
                                                              ) {
        if (bindingResult.hasErrors()) {
            throw new FieldValidationException(ErrorCode.FIELD_VALIDATION_FAILED, handleBindingResult(bindingResult));
        }
        return new ResponseEntity<>(scheduleService.createWorkSchedule(workScheduleRequest, userAccountPrincipal, userid), HttpStatus.OK);
    }

    @PostMapping(value = "/{userid}/update")
    public ResponseEntity<WorkScheduleDto> updateWorkSchedule(@Valid @RequestBody WorkScheduleRequest workScheduleRequest,
                                                              BindingResult bindingResult,
                                                              @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal,
                                                              @PathVariable Long userid
                                                              ) {
        if (bindingResult.hasErrors()) {
            throw new FieldValidationException(ErrorCode.FIELD_VALIDATION_FAILED, handleBindingResult(bindingResult));
        }

        if (workScheduleRequest.eventId() == null) {
            throw new BadRequestException(ErrorCode.NO_EVENT_ID);
        }

        return new ResponseEntity<>(scheduleService.updateWorkSchedule(workScheduleRequest, userAccountPrincipal, userid), HttpStatus.OK);
    }

    @PostMapping(value = "/{userid}/delete")
    public Boolean deleteWorkSchedule(@RequestBody WorkScheduleRequest workScheduleRequest,
                                      @AuthenticationPrincipal UserAccountPrincipal userAccountPrincipal,
                                      @PathVariable Long userid) {

        if (workScheduleRequest.eventId() == null) {
            throw new BadRequestException(ErrorCode.NO_EVENT_ID);
        }

        return scheduleService.deleteWorkSchedule(Long.valueOf(workScheduleRequest.eventId()), userid, userAccountPrincipal);
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
