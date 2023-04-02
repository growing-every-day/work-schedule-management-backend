package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.aop.LoginLog;
import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.dto.LoginRequestDto;
import fastcampus.workschedulemanagementbackend.dto.LoginResponseDto;
import fastcampus.workschedulemanagementbackend.dto.TokenDto;
import fastcampus.workschedulemanagementbackend.jwt.JwtTokenProvider;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AESUtil aesUtil;

    /**
     * 로그인 처리 + 엑세스, 리프레시 토큰 생성 + db저장
     * @param request
     * @return
     * @throws Exception
     */
    @LoginLog
    public LoginResponseDto login(LoginRequestDto request) throws Exception {

        UserAccount userAccount = userAccountRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                new BadCredentialsException("입력한 아이디가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), userAccount.getPassword())) {
            throw new BadCredentialsException("입력한 비밀번호가 올바르지 않습니다.");
        }

        String newRefreshToken = createRefreshToken(userAccount);
        TokenDto tokenDto = TokenDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(userAccount.getUsername(), userAccount.getRole()))
                .refreshToken(newRefreshToken)
                .build();

        return LoginResponseDto.from(userAccount, tokenDto, aesUtil);
    }

    /**
     * refresh 토큰 생성 + userAccount에 저장 + userAccount db 테이블에 저장
     * @param userAccount
     * @return
     */
    public String createRefreshToken(UserAccount userAccount) {
        if (userAccount == null) {
            return null;
        }

        String newRefreshToken = jwtTokenProvider.createRefreshToken(userAccount);
        if (newRefreshToken == null) {
            return null;
        }

        userAccount.setRefreshToken(newRefreshToken);
        userAccountRepository.updateRefreshToken(newRefreshToken, userAccount.getId());
        return newRefreshToken;
    }

    /**
     * access, refresh 토큰 재발급
     * @param accessToken 엑세스 토큰
     * @return
     * @throws Exception
     */
    public TokenDto refreshToken(String accessToken) throws Exception {
        String userName = null;

        try{
            userName = jwtTokenProvider.getUsernameByToken(accessToken);
        } catch (ExpiredJwtException ex){
            // access 토큰이 만료됐으면
            userName = ex.getClaims().getSubject();
        }

        UserAccount userAccount = userAccountRepository.findByUsername(userName).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        String currentRefreshToken = userAccount.getRefreshToken();
        try {
            Claims claims = jwtTokenProvider.verifyToken(currentRefreshToken);
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            // refresh token이 만료됐거나 null이면 재로그인을 해야함
            throw new ExpiredJwtException(null, null, null);
        } catch (Exception e){
            throw e;
        }

        String newRefreshToken = createRefreshToken(userAccount);
        return TokenDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(userName, userAccount.getRole()))
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * 프론트 단에서는 Local Storage에 저장된 access, refresh token 폐기한다.
     * user_account 테이블의 refresh token 컬럼값을 없애준다.
     * @param accessToken 회원정보를 꺼내올 access token
     */
    public boolean logout(String accessToken) {
        String userName = null;
        try {
            Claims claims = jwtTokenProvider.verifyToken(accessToken);
            userName = claims.getSubject();
        } catch (ExpiredJwtException e) {
            userName = e.getClaims().getSubject();
        } catch (Exception e){
            throw e;
        }

        UserAccount userAccount = userAccountRepository.findByUsername(userName).orElseThrow(() ->
                new BadCredentialsException("access token의 잘못된 계정정보입니다."));

        userAccount.setRefreshToken("");
        // refresh token을 빈 문자열로 업데이트 한다. (지워준다)
        userAccountRepository.updateRefreshToken("", userAccount.getId());

        return true;
    }
}
