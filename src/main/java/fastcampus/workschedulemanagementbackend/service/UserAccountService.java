package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.error.ErrorCode;
import fastcampus.workschedulemanagementbackend.error.wsAppException;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PasswordEncoder passwordEncoder;
    private final AESUtil aesUtil;

    @Transactional
    public UserAccountDto join(UserAccountDto userAccountDto){
        //회원가입하려는 username으로 회원가입된 user가 있는지
        userAccountRepository.findByUsername(userAccountDto.username()).ifPresent(it ->{
            throw new wsAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userAccountDto.username()));
        });
        //회원가입 진행 = user를 등록
        UserAccount userAccount = userAccountRepository.save(userAccountDto.toEntity(passwordEncoder, aesUtil));
        return UserAccountDto.fromWithoutPassword(userAccount, aesUtil);
    }

    public List<UserAccountDto> getAllUserAccounts(String name) {
        List<UserAccount> users = name != null ?
                userAccountRepository.findAllByName(name) :
                userAccountRepository.findAll();

        return users
                .stream()
                .map(user -> UserAccountDto.fromWithoutPassword(user, aesUtil))
                .collect(Collectors.toList());
    }

    public Optional<UserAccountDto> getUserAccountById(Long id) {

        if (id == null) {
            throw new BadRequestException("회원 번호는 null 일 수 없습니다");
        }

        return Optional.ofNullable(userAccountRepository.findById(id)
                .map(user -> UserAccountDto.fromWithoutPassword(user, aesUtil))
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
                .map(userAccount -> UserAccountDto.fromWithoutPassword(userAccount, aesUtil))
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
}
