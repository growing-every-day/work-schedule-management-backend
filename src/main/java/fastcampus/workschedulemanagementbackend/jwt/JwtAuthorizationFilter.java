package fastcampus.workschedulemanagementbackend.jwt;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

/**
 * JWT를 이용한 인증
 */
@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;

    /**
     * 페이지에 접근 시도시 "Authorization"헤더에 있는 access token을 검사한다.
     * access token이 만료됐으면 refresh token을 확인하여 refresh token도 만료됐는지 확인한다.
     *  refresh token이 만료되지 않았으면 -> access token을 새로 만들어준다. (3일 이하로 남았으면 연장해준다.)
     *  refresh token도 만료됐으면 -> 로그인 하라는 메세지를 보낸다.
     *
     * access token이 만료되지 않았으면 SecurityContextHolder에 Authentication을 저장한다.
     * @return
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveToken(request);
        if(null != accessToken) {
            try {
                Claims claims = jwtTokenProvider.verifyToken(accessToken);
                UserAccount userAccount = userAccountRepository.findByUsername(claims.getSubject()).orElseThrow(() ->
                        new BadCredentialsException("access token의 잘못된 계정정보입니다."));
                // access 토큰 생성
                String newAccessToken = jwtTokenProvider.createAccessToken(userAccount.getUsername(), userAccount.getRole());
                Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (ExpiredJwtException e) {
                throw e;
            } catch (Exception e){
                throw e;
            }
        }
        filterChain.doFilter(request, response);
    }

}