package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
<<<<<<< main
<<<<<<< main
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
=======
=======
>>>>>>> feat #7- create and test basic UserAccountService
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAccountService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;

<<<<<<< main
    // check id and password
    public boolean isIdAndPasswordCorrect(String id, String password) {
        UserAccount userAccount = userAccountRepository.findById(id).orElse(null);
        if(userAccount == null){
            return false;
        }
        if (userAccount.getPassword() != password) {
            return false;
        }
        return true;
<<<<<<< main
>>>>>>> feat #7- create and test basic UserAccountService
=======
>>>>>>> feat #7- create and test basic UserAccountService
=======
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
>>>>>>> feat #7- apply spring security + jwt
    }
}
