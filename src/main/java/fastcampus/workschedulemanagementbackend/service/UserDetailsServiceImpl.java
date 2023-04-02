package fastcampus.workschedulemanagementbackend.service;

import fastcampus.workschedulemanagementbackend.common.error.exception.BadRequestException;
import fastcampus.workschedulemanagementbackend.common.error.ErrorCode;
import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.repository.UserAccountRepository;
import fastcampus.workschedulemanagementbackend.common.security.UserAccountPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        return UserAccountPrincipal.from(userAccount);
    }

}
