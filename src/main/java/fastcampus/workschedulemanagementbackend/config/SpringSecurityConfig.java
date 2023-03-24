package fastcampus.workschedulemanagementbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 환경 설정을 구성하기 위한 클래스
 * 웹 서비스가 로드 될때 Spring Container 의해 관리가 되는 클래스이며 사용자에 대한 ‘인증’과 ‘인가’에 대한 구성을 Bean 메서드로 주입을 한다.
 */

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // basic authentication filter 비활성화
        http.httpBasic().disable();
        // CsrfFilter : CsrfAttack 을 방어하는 Filter 활성화
        http.csrf().disable();
        // RememberMeAuthenticationFilter: 일반적인 세션보다 훨씬 오랫동안 로그인 사실을 기억하도록 활성화
       // http.rememberMe();

//        // login
//        http.formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/")
//                .permitAll();
//
//        // logout
//        http.logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/");

        // authorize
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/join","/", "/login" , "/signup").permitAll()
                        .anyRequest().authenticated());

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();

    }

    /**
     * 3. authenticate 의 인증 메서드를 제공하는 매니져로'Provider'의 인터페이스를 의미
     * - 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
