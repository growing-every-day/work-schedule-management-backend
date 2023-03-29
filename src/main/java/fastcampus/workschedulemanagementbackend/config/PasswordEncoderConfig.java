package fastcampus.workschedulemanagementbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder Config
 * db에 pw를 그냥 저장하지 않고 암호화해서 해야함
 * 그래서 암호화해주는 인코더를 빈으로 생성
 */
@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
