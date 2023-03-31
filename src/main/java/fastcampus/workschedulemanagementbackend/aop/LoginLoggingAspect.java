package fastcampus.workschedulemanagementbackend.aop;

import fastcampus.workschedulemanagementbackend.dto.LoginResponseDto;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import fastcampus.workschedulemanagementbackend.service.UserLoginLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LoginLoggingAspect {

    private final UserLoginLogService userLoginLogService;

    @Around("@annotation(LoginLog)")
    public Object logSuccessfulLogin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        LoginResponseDto result = (LoginResponseDto) proceedingJoinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        userLoginLogService.logSuccessfulLogin(result, userAgent, ipAddress);

        return result;
    }



}
