package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.aop.LoginLog;
import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.dto.LoginRequestDto;
import fastcampus.workschedulemanagementbackend.dto.LoginResponseDto;
import fastcampus.workschedulemanagementbackend.dto.TokenDto;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.jwt.JwtTokenProvider;
import fastcampus.workschedulemanagementbackend.error.ErrorCode;
import fastcampus.workschedulemanagementbackend.error.wsAppException;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AESUtil aesUtil;

    @Transactional
    public UserAccountDto join(UserAccountDto userAccountDto){
        //회원가입하려는 username으로 회원가입된 user가 있는지
        userAccountRepository.findByUsername(userAccountDto.username()).ifPresent(it ->{
            throw new wsAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s 는 중복된 아이디입니다.", userAccountDto.username()));
        });
        //회원가입 진행 = user를 등록
        UserAccount userAccount = userAccountRepository.save(userAccountDto.toEntity(passwordEncoder, aesUtil));
        return UserAccountDto.from(userAccount);
    }

    public List<UserAccountDto> getAllUserAccounts(String name) {

        List<UserAccount> users = name != null ?
                userAccountRepository.findAllByNameContainsIgnoreCase(name) :
                userAccountRepository.findAll();

        if (users.isEmpty()) {
            throw new BadRequestException("회원이 존재하지 않습니다");
        }

        return users
                .stream()
                .map(UserAccountDto::fromWithoutPassword)
                .collect(Collectors.toList());
    }

    public Optional<UserAccountDto> getUserAccountById(Long id) {

        if (id == null) {
            throw new BadRequestException("회원 번호는 null 일 수 없습니다");
        }

        return Optional.ofNullable(userAccountRepository.findById(id)
                .map(UserAccountDto::fromWithoutPassword)
                .orElseThrow(() -> new BadRequestException(String.format("회원 번호(%d)를 찾을 수 없습니다", id))));
    }

    @Transactional
    public Optional<UserAccountDto> updateUserAccount(Long id, UserAccountDto userAccountDto) {

        if (id == null) {
            throw new BadRequestException("회원 번호는 null 일 수 없습니다");
        }

        if (userAccountDto == null) {
            throw new BadRequestException("회원 정보는 null 일 수 없습니다");
        }

        return Optional.ofNullable(userAccountRepository.findById(id)
                .map(existingUserAccount -> {
                    existingUserAccount.update(userAccountDto);
                    return existingUserAccount;
                })
                .map(UserAccountDto::fromWithoutPassword)
                .orElseThrow(() -> new BadRequestException(String.format("회원 번호(%d)를 찾을 수 없습니다", id))));
    }

    @Transactional
    public Optional<Boolean> deleteUserAccount(Long id) {

        if (id == null) {
            throw new BadRequestException("회원 번호는 null 일 수 없습니다");
        }

        return Optional.ofNullable(userAccountRepository.findById(id)
                .map(existingUserAccount -> {
                    userAccountRepository.delete(existingUserAccount);
                    return true;
                })
                .orElseThrow(() -> new BadRequestException(String.format("회원 번호(%d)를 찾을 수 없습니다", id))));
    }

    /**
     * 로그인 처리 + 엑세스, 리프레시 토큰 생성 + db저장
     * @param request
     * @return
     * @throws Exception
     */
    @LoginLog
    public LoginResponseDto login(LoginRequestDto request) throws Exception {
        UserAccount userAccount = userAccountRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        if (!passwordEncoder.matches(request.getPassword(), userAccount.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        String newRefreshToken = createRefreshToken(userAccount);

        return LoginResponseDto.builder()
                .id(userAccount.getId())
                .userName(userAccount.getUsername())
                .name(userAccount.getName())
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .token(TokenDto.builder()
                        .accessToken(jwtTokenProvider.createAccessToken(userAccount.getUsername(), userAccount.getRole()))
                        .refreshToken(newRefreshToken)
                        .build())
                .build();
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
     * @param token
     * @return
     * @throws Exception
     */
    public TokenDto refreshToken(TokenDto token) throws Exception {
        String userName = null;

        try{
            userName = jwtTokenProvider.getUsernameByToken(token.getAccessToken());
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
     * 클라이언트 Local Storage에 저장된 access, refresh token폐기한다.
     * useraccount테이블의 refresh token 컬럼값을 없애준다.
     * @param tokenDto 회원정보를 꺼내올 access token
     */
    public boolean logout(TokenDto tokenDto) {
        UserAccount userAccount = null;
        String userName = null;
        try {
            Claims claims = jwtTokenProvider.verifyToken(tokenDto.getRefreshToken());
            userName = claims.getSubject();
        } catch (ExpiredJwtException e) {
            userName = e.getClaims().getSubject();
        } catch (Exception e){
            throw e;
        }

        userAccount = userAccountRepository.findByUsername(userName).orElseThrow(() ->
                new BadCredentialsException("access token의 잘못된 계정정보입니다."));

        userAccount.setRefreshToken("");
        // refresh token을 빈 문자열로 업데이트 한다. (지워준다)
        userAccountRepository.updateRefreshToken("", userAccount.getId());
        return true;
    }
}
