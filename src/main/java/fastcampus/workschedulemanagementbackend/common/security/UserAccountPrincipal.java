package fastcampus.workschedulemanagementbackend.common.security;

import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.domain.constants.UserRoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public record UserAccountPrincipal(
        Long id,
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String name,
        int remainedVacationCount
) implements UserDetails {

    public static UserAccountPrincipal of(Long id, String username, String password, String email, String name, UserRoleType role, Integer remainedVacationCount) {
        Set<UserRoleType> roleTypes = Set.of(role);
        return new UserAccountPrincipal(
                id,
                username,
                password,
                roleTypes.stream()
                        .map(UserRoleType::getValue)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email,
                name,
                remainedVacationCount
        );
    }

    public static UserAccountPrincipal from(UserAccount userAccount) {
        return UserAccountPrincipal.of(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getEmail(),
                userAccount.getName(),
                userAccount.getRole(),
                userAccount.getRemainedVacationCount()
        );
    }

    public Long getId() {
        return id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
