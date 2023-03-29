package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    public List<UserAccountDto> getAllUserAccounts() {
        List<UserAccount> users = userAccountRepository.findAll();

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

        log.info("userAccountDto: {}", userAccountDto);
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
    // check id and password
    public boolean isIdAndPasswordCorrect(String id, String password) {
        UserAccount userAccount = userAccountRepository.findById(id).orElse(null);
        if (userAccount == null) {
            return false;
        }
        if (userAccount.getPassword() != password) {
            return false;
        }
        return true;
    }

    /**
     * 로그인 처리 + 엑세스, 리프레시 토큰 생성 + db저장
     * @param request
     * @return
     * @throws Exception
     */
    public LoginResponseDto login(LoginRequestDto request) throws Exception {
        UserAccount userAccount = userAccountRepository.findByUserName(request.getUsername()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), userAccount.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        String newRefreshToken = createRefreshToken(userAccount);

        return LoginResponseDto.builder()
                .id(userAccount.getId())
                .userName(userAccount.getUserName())
                .name(userAccount.getName())
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .token(TokenDto.builder()
                        .accessToken(jwtTokenProvider.createAccessToken(userAccount.getUserName(), userAccount.getRole()))
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

        UserAccount userAccount = userAccountRepository.findByUserName(userName).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        
        String newRefreshToken = createRefreshToken(userAccount);
        return TokenDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(userName, userAccount.getRole()))
                .refreshToken(newRefreshToken)
                .build();
    }
}
