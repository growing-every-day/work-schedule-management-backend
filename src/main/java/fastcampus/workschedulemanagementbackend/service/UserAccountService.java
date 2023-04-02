package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.common.error.BadRequestException;
import fastcampus.workschedulemanagementbackend.common.error.ErrorCode;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.common.utils.AESUtil;
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
            throw new BadRequestException(ErrorCode.DUPLICATED_USER_NAME);
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
            throw new BadRequestException(ErrorCode.USER_ID_NULL);
        }

        return Optional.ofNullable(userAccountRepository.findById(id)
                .map(user -> UserAccountDto.fromWithoutPassword(user, aesUtil))
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public Optional<UserAccountDto> updateUserAccount(Long id, UserAccountDto userAccountDto) {

        if (id == null) {
            throw new BadRequestException(ErrorCode.USER_ID_NULL);
        }

        if (userAccountDto == null) {
            throw new BadRequestException(ErrorCode.USER_ACCOUNT_NULL);
        }

        return Optional.ofNullable(userAccountRepository.findById(id)
                .map(existingUserAccount -> {
                    existingUserAccount.update(userAccountDto);
                    return existingUserAccount;
                })
                .map(userAccount -> UserAccountDto.fromWithoutPassword(userAccount, aesUtil))
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public Optional<Boolean> deleteUserAccount(Long id) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.USER_ID_NULL);
        }

        return Optional.ofNullable(userAccountRepository.findById(id)
                .map(existingUserAccount -> {
                    userAccountRepository.delete(existingUserAccount);
                    return true;
                })
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND)));
    }
}
