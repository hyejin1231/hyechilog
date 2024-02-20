package com.myrou.hyechilog.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.myrou.hyechilog.api.exception.UnAuthorized;
import com.myrou.hyechilog.config.data.UserSession;

/**
 * [2024.02.20]
 * API 인증 : ArgumentResolver 이용하기
 */
public class AuthResolver implements HandlerMethodArgumentResolver
{
	
	@Override
	public boolean supportsParameter(MethodParameter parameter)
	{
		// 1) 요청하는 메서드의 파라미터의 타입이 UserSession 인지 확인
		return parameter.getParameterType().equals(UserSession.class);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception
	{
		// 2) 파라미터의 타입이 UserSession 이라면 해당 메서드 실행
		String accessToken = webRequest.getHeader("Authorization");
		if (accessToken == null || accessToken.isEmpty()) {
			throw new UnAuthorized();
		}
		
		// 데이터베이스 사용자 확인 작업
		// ...
		
		// accessToken 이 있다면 UserSession 의 name에 accessToken을 담아 넘긴다.
		return new UserSession(1L);
	}
}
