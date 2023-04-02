package fastcampus.workschedulemanagementbackend.common.jwt;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Date;

/**
 * 토큰을 생성하고 검증하는 등 토큰을 다루는 주요 함수가 정의된 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private String secretKey = "workschedulesecretkey";

    // 엑세스 토큰 유효시간 1시간 1000L * 60 * 60;
    static final long ACCESS_TOKEN_VALID_MILLIE_SEC_TIME = 1000L * 60 * 30;

    // 리프레시 토큰 유효시간 일주일 1000L * 60 * 60 * 24 * 7;
    static final long REFRESH_TOKEN_VALID_MILLIE_SEC_TIME = 1000L * 60 * 35;

    private final UserDetailsService userDetailsService;

    /**
     * 객체 초기화, secretKey를 Base64로 인코딩한다.
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 엑세스 토큰 생성
     * @param userName
     * @param roleType
     * @return
     */
    public String createAccessToken(String userName, UserRoleType roleType) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", roleType);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLIE_SEC_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 리프레시 토큰 생성
     * @param userAccount
     * @return
     */
    public String createRefreshToken(UserAccount userAccount) {
        Claims claims = Jwts.claims().setSubject(userAccount.getUsername());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILLIE_SEC_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    /**
     * JWT 토큰에서 인증 정보 생성
     * @param token
     * @return UsernamePasswordAuthenticationToken
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰에서 user name 추출
     * @param token
     * @return String
     */
    public String getUsernameByToken(String token) {
        Claims claims = verifyToken(token);
        return claims.getSubject();
    }

    /**
     * Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken =  request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 토큰을 복호화 한 후 결과물을 반환한다.
     * @param token 검증할 토큰 문자열
     * @return jwt 토큰 페이로드 값
     * @throws SignatureException 서명이 잘못된 경우
     * @throws UnsupportedJwtException jwt 형식이 잘못된 경우
     * @throws IllegalArgumentException 토큰 값이 없는 경우
     * @throws MalformedJwtException 토큰이 올바른 jws 형식이 아닌 경우
     * @throws ExpiredJwtException 토큰이 만료된 경우 발생
     */
    public Claims verifyToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}