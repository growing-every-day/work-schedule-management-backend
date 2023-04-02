package fastcampus.workschedulemanagementbackend.controller.validator;

import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;
import fastcampus.workschedulemanagementbackend.dto.request.workschedule.WorkScheduleRequest;
import jakarta.validation.ConstraintViolation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;

import java.util.Objects;
import java.util.Set;

public class WorkScheduleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return WorkScheduleRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        WorkScheduleRequest request = WorkScheduleRequest.class.cast(target);

        if(Objects.isNull(ScheduleType.valueOfLabel(request.category().getLabel()))){
            errors.rejectValue("category", "NotExist", "category must be LEAVE or DUTY");
            return;
        }

        if (Objects.isNull(request.start())) {
            errors.rejectValue("start", "NotNull", "start_date is null");
            return;
        }
        if (Objects.isNull(request.end())) {
            errors.rejectValue("end", "NotNull", "end_date is null");
            return;
        }
        if (request.start().compareTo(request.end()) >= 0) {
            errors.rejectValue("endDate", "Constraint Error", "endDate is earlier than startDate ");
            return;
        }
    }
}
