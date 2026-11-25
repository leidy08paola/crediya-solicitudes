package co.com.crediya.api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    private static final List<String> PUBLIC_PATTERNS = List.of("/api/v1/login");

    private final JwtInterceptor jwtInterceptor;

    public WebSecurityConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns(PUBLIC_PATTERNS);
    }
}
