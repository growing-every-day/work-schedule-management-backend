package fastcampus.workschedulemanagementbackend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
                Claims claims = jwtTokenProvider.validateBearerAccessToken(accessToken);

                accessToken = accessToken.split(" ")[1].trim();
                Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (ExpiredJwtException exp) {
                // access token이 만료됨 -> refresh token 확인
                 UserAccount userAccount = userAccountRepository.findByUserName(exp.getClaims().getSubject()).orElseThrow(() ->
                        new BadCredentialsException("잘못된 계정정보입니다."));

                try {
                    Claims refreshClaims = jwtTokenProvider.verifyToken(userAccount.getRefreshToken());

                    // 리프레시 토큰 만료시간이 3일 이내로 남았으면 새로 생성해준다.
                    if (jwtTokenProvider.isTokenRenewRequired(refreshClaims.getExpiration(), 3, ChronoUnit.DAYS)) {
                        userAccountService.createRefreshToken(userAccount);
                    }

                    // access 토큰 생성
                    String newAccessToken = jwtTokenProvider.createAccessToken(userAccount.getUserName(), userAccount.getRole());
                    Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                }
                catch (ExpiredJwtException exp2){
                    response.setStatus(200);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("text/html; charset=UTF-8");
                    response.getWriter().write("리프레시 토큰이 만료되었습니다. 재로그인 하세요.");
                }
                catch (Exception ex) {

                }
            }
        }
        filterChain.doFilter(request, response);
    }


}