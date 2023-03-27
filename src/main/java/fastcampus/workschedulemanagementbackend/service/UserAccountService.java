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
}
