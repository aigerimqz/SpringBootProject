package kz.kbtu.owner.service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor jwtFeignInterceptor() {
        return requestTemplate -> {

            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof UsernamePasswordAuthenticationToken token) {

                if (token.getCredentials() instanceof String jwt) {
                    requestTemplate.header("Authorization", "Bearer " + jwt);
                }
            }
        };
    }
}