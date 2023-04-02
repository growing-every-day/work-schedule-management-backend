package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import fastcampus.workschedulemanagementbackend.dto.request.workschedule.WorkScheduleRequest;
import fastcampus.workschedulemanagementbackend.common.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.common.error.ErrorCode;
import fastcampus.workschedulemanagementbackend.common.error.InvalidRemainedVacationException;
import fastcampus.workschedulemanagementbackend.repository.ScheduleRepository;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.repository.WorkScheduleRepository;
import fastcampus.workschedulemanagementbackend.common.security.UserAccountPrincipal;
import fastcampus.workschedulemanagementbackend.common.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserAccountRepository userAccountRepository;

    private final AESUtil aesUtil;
    private final WorkScheduleRepository workScheduleRepository;

    @Transactional
    public List<WorkScheduleDto> getAllSchedules(Long id, String year, String month) throws ParseException {
        if (month.length() == 1) {
            month = "0" + month;
        }
        String dateStr = year + "/" + month + "/01";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = formatter.parse(dateStr);
        List<WorkSchedule> schedules = id != null ?
                scheduleRepository.findAllByUserAccountIdWithDate(id, date) :
                scheduleRepository.findAllWithUserAccount();
        if (schedules.isEmpty()) {
            throw new BadRequestException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        return schedules
                .stream()
                .map(schedule -> WorkScheduleDto.from(schedule, aesUtil))
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkScheduleDto createWorkSchedule(WorkScheduleRequest workScheduleRequest, UserAccountPrincipal userAccountPrincipal, Long userid) {
        checkAdmin(userid, userAccountPrincipal);

        UserAccount userAccount = userAccountRepository.findById(userid)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        checkDuplicateDates(userid, workScheduleRequest.start(), workScheduleRequest.end());
        checkRemainedVacationCount(userAccount, (int) ChronoUnit.DAYS.between(workScheduleRequest.start(), workScheduleRequest.end()) + 1);

        String name = aesUtil.decrypt(userAccountPrincipal.name());
        return Optional.ofNullable(userAccount)
                .map(user -> {
                            if (ScheduleType.valueOf(workScheduleRequest.category()).equals(ScheduleType.LEAVE)) {
                                LocalDate start = workScheduleRequest.start();
                                LocalDate end = workScheduleRequest.end();
                                int leave = (int) ChronoUnit.DAYS.between(start, end) + 1;
                                user.setRemainedVacationCount(user.getRemainedVacationCount() - leave);
                            }
                            return workScheduleRequest.toDto(name).toEntity(user);
                        }
                )//DB와 통신할 Entity형태로 변환
                .map(workSchedule -> WorkScheduleDto.from(scheduleRepository.save(workSchedule), aesUtil))
                .orElseThrow(() -> new BadRequestException(ErrorCode.SERVER_ERROR));
    }

    @Transactional
    public WorkScheduleDto updateWorkSchedule(WorkScheduleRequest workScheduleRequest, UserAccountPrincipal userAccountPrincipal, Long userid) {
        UserAccount userAccount = userAccountRepository.findById(userid)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        String name = aesUtil.decrypt(userAccountPrincipal.name());
        WorkSchedule workSchedule = workScheduleRepository.findById(Long.valueOf(workScheduleRequest.eventId()))
                .orElseThrow(() -> new BadRequestException(ErrorCode.SCHEDULE_NOT_FOUND));

        Long workScheduleUserId = workSchedule.getUserAccount().getId();
        checkAdminWithSchedule(userid, userAccountPrincipal, workScheduleUserId);

        log.error("workScheduleRequest.category() : {}", workScheduleRequest.category());
        log.error("workSchedule.getCategory() : {}", workSchedule.getCategory());

        return Optional.of(workSchedule)
                .map(workScheduleEnt -> {
                    LocalDate start = workScheduleRequest.start();
                    LocalDate end = workScheduleRequest.end();
                    int newLeave = (int) ChronoUnit.DAYS.between(start, end) + 1;
                    LocalDate exStart = workScheduleEnt.getStart();
                    LocalDate exEnd = workScheduleEnt.getEnd();
                    int exLeave = (int) ChronoUnit.DAYS.between(exStart, exEnd) + 1;

                    if (ScheduleType.valueOf(workScheduleRequest.category()).equals(ScheduleType.DUTY))
                        newLeave = 0;
                    if (workScheduleEnt.getCategory().equals(ScheduleType.DUTY))
                        exLeave = 0;
                    checkRemainedVacationCount(userAccount, newLeave - exLeave);
                    workScheduleEnt.getUserAccount().setRemainedVacationCount(workScheduleEnt.getUserAccount().getRemainedVacationCount() - newLeave + exLeave);

                    return WorkScheduleDto.from(workScheduleEnt.update(workScheduleRequest, name), aesUtil);
                })
                .orElseThrow(() -> new BadRequestException(ErrorCode.SERVER_ERROR));
    }

    @Transactional
    public Boolean deleteWorkSchedule(Long eventId, Long userid, UserAccountPrincipal userAccountPrincipal) {
        UserAccount userAccount = userAccountRepository.findById(userid)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        WorkSchedule workSchedule = scheduleRepository.findById(eventId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (ChronoUnit.DAYS.between(workSchedule.getStart(), LocalDate.now()) > 0)
            throw new BadRequestException(ErrorCode.ALREADY_IN_THE_SCHEDULE);

        int addVacationCount = (int) ChronoUnit.DAYS.between(workSchedule.getEnd(), workSchedule.getStart()) + 1;

        Long workScheduleUserId = workSchedule.getUserAccount().getId();
        checkAdminWithSchedule(userid, userAccountPrincipal, workScheduleUserId);
        scheduleRepository.deleteById(eventId);
        userAccount.setRemainedVacationCount(userAccount.getRemainedVacationCount() + addVacationCount);
        return true;
    }

    private static void checkAdmin(Long id, UserAccountPrincipal userAccountPrincipal) {
        boolean isAdmin = userAccountPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userAccountPrincipal.getId().equals(id)) {
            throw new BadRequestException(ErrorCode.NO_ADMIN);
        }
    }

    private void checkAdminWithSchedule(Long userid, UserAccountPrincipal userAccountPrincipal, Long workScheduleUserId) {
        boolean isAdmin = userAccountPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && (!Objects.equals(userAccountPrincipal.getId(), userid) || !Objects.equals(workScheduleUserId, userid) || !Objects.equals(workScheduleUserId, userAccountPrincipal.id()))) {
            throw new BadRequestException(ErrorCode.NO_ADMIN_NO_OWN_SCHEDULE);
        }
    }

    private void checkRemainedVacationCount(UserAccount userAccount, int leaveDay) {
        if (leaveDay != 0 && leaveDay > userAccount.getRemainedVacationCount()) {
            throw new InvalidRemainedVacationException(ErrorCode.INVALID_REMAINED_VACATION, String.format("사용가능한 휴가일 수가 부족합니다. 사용가능한 휴가일 수는 %d일 입니다.", userAccount.getRemainedVacationCount()));
        }
    }

    private void checkDuplicateDates(Long userId, LocalDate start, LocalDate end) {
        List<WorkSchedule> schedules = scheduleRepository.findAllByUserAccountId(userId);
        for (WorkSchedule schedule : schedules) {
            if (schedule.getStart().isBefore(end) && start.isBefore(schedule.getEnd())) {
                throw new BadRequestException(ErrorCode.DUPLICATED_SCHEDULE);
            }
        }
    }
}
