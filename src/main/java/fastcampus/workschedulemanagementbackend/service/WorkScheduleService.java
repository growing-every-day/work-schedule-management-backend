package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.WorkSchedule;
import fastcampus.workschedulemanagementbackend.domain.constants.ScheduleType;
import fastcampus.workschedulemanagementbackend.dto.WorkScheduleDto;
import fastcampus.workschedulemanagementbackend.dto.request.workschedule.WorkScheduleRequest;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.repository.ScheduleRepository;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.repository.WorkScheduleRepository;
import fastcampus.workschedulemanagementbackend.security.UserAccountPrincipal;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import lombok.RequiredArgsConstructor;
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
    public WorkScheduleDto createWorkSchedule(WorkScheduleRequest workScheduleRequest, UserAccountPrincipal userAccountPrincipal, Long userid) {
        checkAdmin(userid, userAccountPrincipal, "다른 회원의 스케쥴을 생성할 권한이 없습니다.");

        UserAccount userAccount = userAccountRepository.findById(userid)
                .orElseThrow(() -> new BadRequestException(String.format("회원 번호(%d)를 찾을 수 없습니다", userid)));
        checkRemainedVacationCount(userAccount, (int) ChronoUnit.DAYS.between(workScheduleRequest.start(), workScheduleRequest.end()) + 1);

        String name = aesUtil.decrypt(userAccountPrincipal.name());
        return Optional.ofNullable(userAccount)
                .map(user -> {
                            if (workScheduleRequest.category().equals(ScheduleType.LEAVE)) {
                                LocalDate start = workScheduleRequest.start();
                                LocalDate end = workScheduleRequest.end();
                                int leave = (int) ChronoUnit.DAYS.between(start, end) + 1;
                                user.setRemainedVacationCount(user.getRemainedVacationCount() - leave);
                            }
                            WorkSchedule workSchedule = workScheduleRequest.toDto(name).toEntity(user);
                            return workSchedule;
                        }
                )//DB와 통신할 Entity형태로 변환
                .map(workSchedule -> WorkScheduleDto.from(scheduleRepository.save(workSchedule), aesUtil))
                .orElseThrow(() -> new BadRequestException("DB와 연결에 실패했습니다."));
    }

    @Transactional
    public WorkScheduleDto updateWorkSchedule(WorkScheduleRequest workScheduleRequest, UserAccountPrincipal userAccountPrincipal, Long userid) {
        UserAccount userAccount = userAccountRepository.findById(userid)
                .orElseThrow(() -> new BadRequestException(String.format("회원 번호(%d)를 찾을 수 없습니다.", userid)));

        String name = aesUtil.decrypt(userAccountPrincipal.name());
        WorkSchedule workSchedule = workScheduleRepository.findById(Long.valueOf(workScheduleRequest.eventId()))
                .orElseThrow(() -> new BadRequestException(String.format("스케쥴 정보(%d)를 찾을 수 없습니다.", workScheduleRequest.eventId())));

        Long workScheduleUserId = workSchedule.getUserAccount().getId();
        checkAdminWithSchedule(userid, userAccountPrincipal, workScheduleUserId, "다른 회원의 스케쥴을 수정할 권한이 없습니다.");

        return Optional.ofNullable(workSchedule)
                .map(workScheduleEnt -> {
                    LocalDate start = workScheduleRequest.start();
                    LocalDate end = workScheduleRequest.end();
                    int newLeave = (int) ChronoUnit.DAYS.between(start, end) + 1;
                    LocalDate exStart = workScheduleEnt.getStart();
                    LocalDate exEnd = workScheduleEnt.getEnd();
                    int exLeave = (int) ChronoUnit.DAYS.between(exStart, exEnd) + 1;

                    if (workScheduleRequest.category().equals(ScheduleType.DUTY))
                        newLeave = 0;
                    if (workScheduleEnt.getCategory().equals(ScheduleType.DUTY))
                        exLeave = 0;
                    checkRemainedVacationCount(userAccount, newLeave - exLeave);
                    workScheduleEnt.getUserAccount().setRemainedVacationCount(workScheduleEnt.getUserAccount().getRemainedVacationCount() - newLeave + exLeave);

                    WorkScheduleDto workScheduleDto = WorkScheduleDto.from(workScheduleEnt.update(workScheduleRequest, name), aesUtil);
                    return workScheduleDto;
                })
                .orElseThrow(() -> new BadRequestException("DB와 연결에 실패했습니다."));
    }

    @Transactional
    public Boolean deleteWorkSchedule(Long eventId, Long userid, UserAccountPrincipal userAccountPrincipal) {
        UserAccount userAccount = userAccountRepository.findById(userid)
                .orElseThrow(() -> new BadRequestException(String.format("회원 번호(%d)를 찾을 수 없습니다.", userid)));

        WorkSchedule workSchedule = scheduleRepository.findById(eventId)
                .orElseThrow(() -> new BadRequestException(String.format("스케쥴 정보(%s)를 찾을 수 없습니다.", eventId)));

        if (ChronoUnit.DAYS.between(workSchedule.getStart(), LocalDate.now()) < 0)
            throw new BadRequestException("휴가, 당직 중인 일정은 변경할 수 업습니다.");

        int addVacationCount = (int) ChronoUnit.DAYS.between(workSchedule.getEnd(), workSchedule.getStart()) + 1;

        Long workScheduleUserId = workSchedule.getUserAccount().getId();
        checkAdminWithSchedule(userid, userAccountPrincipal, workScheduleUserId, "다른 회원의 스케쥴 삭제할 권한이 없습니다.");
        scheduleRepository.deleteById(eventId);
        userAccount.setRemainedVacationCount(userAccount.getRemainedVacationCount() + addVacationCount);
        return true;
    }

    private static void checkAdmin(Long id, UserAccountPrincipal userAccountPrincipal, String errorMessage) {
        boolean isAdmin = userAccountPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userAccountPrincipal.getId().equals(id)) {
            throw new BadRequestException(errorMessage);
        }
    }

    private void checkAdminWithSchedule(Long userid, UserAccountPrincipal userAccountPrincipal, Long workScheduleUserId, String errorMessage) {
        boolean isAdmin = userAccountPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && (!Objects.equals(userAccountPrincipal.getId(), userid) || !Objects.equals(workScheduleUserId, userid) || !Objects.equals(workScheduleUserId, userAccountPrincipal.id()))) {
            throw new BadRequestException(errorMessage);
        }
    }

    private void checkRemainedVacationCount(UserAccount userAccount, int leaveDay) {
        if (leaveDay != 0 && leaveDay > userAccount.getRemainedVacationCount()) {
            throw new BadRequestException(String.format("사용가능한 휴가일 수가 부족합니다. 사용가능한 휴가일 수는 %d일 입니다.", userAccount.getRemainedVacationCount()));
        }
    }
}
