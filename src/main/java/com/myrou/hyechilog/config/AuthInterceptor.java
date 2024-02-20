package com.myrou.hyechilog.config;

import com.myrou.hyechilog.api.exception.UnAuthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * [2024.02.19]
 * API 인증을 위한 Interceptor 추가
 * -> 우리 프로젝트에서 사용하지 않을 것이기 때문에 Deprecated
 */
@Deprecated
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(" >> preHandle");

        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && accessToken.equals("hyechii")) {
            return true;
        }

        throw new UnAuthorized();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info(" >> postHandle");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info(" >> afterCompletion");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
