package fastcampus.workschedulemanagementbackend.controller.validator;

import fastcampus.workschedulemanagementbackend.dto.request.workschedule.WorkScheduleRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class WorkScheduleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return WorkScheduleRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        WorkScheduleRequest request = (WorkScheduleRequest) target;

        if (Objects.isNull(request.start())) {
            errors.rejectValue("start", "NotNull", "start_date is null");
            return;
        }
        if (Objects.isNull(request.end())) {
            errors.rejectValue("end", "NotNull", "end_date is null");
            return;
        }
        if ((int) ChronoUnit.DAYS.between(request.start(), request.end()) < 0) {
            errors.rejectValue("end", "Constraint Error", "종료일이 시작일보다 빠릅니다.");
            return;
        }
    }
}
