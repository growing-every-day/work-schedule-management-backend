package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.UserLoginLog;
import fastcampus.workschedulemanagementbackend.dto.LoginResponseDto;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.repository.UserLoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLoginLogService {

    private final UserLoginLogRepository userLoginLogRepository;
    private final UserAccountRepository userAccountRepository;

    public void logSuccessfulLogin(LoginResponseDto loginResponseDto, String userAgent, String ipAddress) {
        UserAccount userAccount = userAccountRepository.findById(loginResponseDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userLoginLogRepository.save(UserLoginLog.of(userAccount, userAgent, ipAddress));
    }

}
