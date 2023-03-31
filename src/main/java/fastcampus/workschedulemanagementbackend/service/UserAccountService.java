package fastcampus.workschedulemanagementbackend.service;

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
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Transactional
    public UserAccountDto join(UserAccountDto userAccountDto){
        //회원가입하려는 username으로 회원가입된 user가 있는지
        userAccountRepository.findByUsername(userAccountDto.username()).ifPresent(it ->{
            throw new wsAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userAccountDto.username()));
        });
        //회원가입 진행 = user를 등록
        UserAccount userAccount = userAccountRepository.save(userAccountDto.toEntity(passwordEncoder));
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
        
        String newRefreshToken = createRefreshToken(userAccount);
        return TokenDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(userName, userAccount.getRole()))
                .refreshToken(newRefreshToken)
                .build();
    }
}
