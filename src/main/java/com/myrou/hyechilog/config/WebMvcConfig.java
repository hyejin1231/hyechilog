package com.myrou.hyechilog.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.myrou.hyechilog.api.repository.user.SessionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final SessionRepository sessionRepository;
    /*
    v1. API 인증 : Interceptor 이용하기
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .excludePathPatterns("/api/v1/welcome");
    }
     */
    
    /**
     * v2.API 인증 : ArgumentResolver 이용하기
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> resolvers)
    {
        resolvers.add(new AuthResolver(sessionRepository));
    }
}
