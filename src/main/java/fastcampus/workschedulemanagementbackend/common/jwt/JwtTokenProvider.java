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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

// 토큰을 생성하고 검증하는 클래스입니다.
// 해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "workschedulesecretkey";

    // 엑세스 토큰 유효시간 1분
    static final long ACCESS_TOKEN_VALID_MILLIE_SEC_TIME = 1000L * 60;

    // 리프레시 토큰 유효시간 3분
    static final long REFRESH_TOKEN_VALID_MILLIE_SEC_TIME = 1000L * 180;

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
     * @return
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰에서 user name 추출
     * @param token
     * @return
     */
    public String getUsernameByToken( String token) {
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
     * BEARER 엑세스 토큰 유효성 검사
     * @param token 헤더에 붙어서 온 엑세스 토큰
     * @return
     */
    public Claims validateBearerAccessToken(String token) {
        // Bearer 검증
        if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER "))
            throw new SignatureException("BEARER 서명이 없습니다.");

        // Bearer 키워드 제외한 토큰 검사하기 위해 string 분리
        token = token.split(" ")[1].trim();

        Claims claimsBody = null;
        try{
            claimsBody = verifyToken(token);
        } catch (ExpiredJwtException exp){
            throw exp;
        } catch (Exception e){
            throw e;
        }

        return claimsBody;
    }

    /**
     *
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

    public boolean isValidateToken(String token) {
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return true;
        } catch (SignatureException e) {
            log.info("토큰의 서명이 잘못됐습니다.");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            log.info("jwt 형식이 잘못됐습니다.");
        } catch (IllegalArgumentException e) {
            log.info("토큰 값이 없습니다.");
        } catch (ExpiredJwtException e){
            log.info("토큰이 만료됐습니다.");
        }
        return false;
    }

    /**
     * 토큰의 만료일자가 timeUnit단위의 time보다 적게 남았으면 return true
     * @param expTime 토큰의 만료일자
     * @param time timeUnit단위의 시간
     * @param timeUnit 시간 단위(초, 분, 시간, 일)
     * @return
     */
    public boolean isTokenRenewRequired(Date expTime, long time, ChronoUnit timeUnit) {
        Instant exp = expTime.toInstant();
        Instant now = Instant.now();

        long diff = now.until(exp, timeUnit);

        return diff < time;
    }
}