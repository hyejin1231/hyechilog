package com.myrou.hyechilog.config;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.myrou.hyechilog.api.domain.blog.Session;
import com.myrou.hyechilog.api.exception.UnAuthorized;
import com.myrou.hyechilog.api.repository.user.SessionRepository;
import com.myrou.hyechilog.config.data.UserSession;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [2024.02.20]
 * API 인증 : ArgumentResolver 이용하기
 */
@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver
{
	private final SessionRepository sessionRepository;
	private static final String KEY = "tESFRxlqGAmAiPkktb+gvKfvIRh2JpLGch2xGJtWBUg=";
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
		
		/* v1. 직접 토큰 생성해서 인증 (UUID 생성)
		String accessToken = webRequest.getHeader("Authorization");
		if (accessToken == null || accessToken.isEmpty()) {
			throw new UnAuthorized();
		}
		 */
		
		/* v2.
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		if (servletRequest == null) {
			log.info("servletRequest null");
			throw new UnAuthorized();
		}
		
		Cookie[] cookies = servletRequest.getCookies();
		if (cookies.length == 0) {
			log.info("no cookie");
			throw new UnAuthorized();
		}
		
		String accessToken = cookies[0].getValue();
		
		// 데이터베이스 사용자 확인 작업
		Session session = sessionRepository.findByAccessToken(accessToken).orElseThrow(UnAuthorized::new);
		
		// accessToken 이 있다면 UserSession 의 name에 accessToken을 담아 넘긴다.
		return new UserSession(session.getUser().getId());
		 */
		
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		if (servletRequest == null) {
			throw new UnAuthorized();
		}
		
		Cookie[] cookies = servletRequest.getCookies();
		if (cookies.length == 0) {
			throw new UnAuthorized();
		}
		
		String jwt = cookies[0].getValue();
		
		try {
			SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));
			
			Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt);
			
			String userId = claimsJws.getPayload().getSubject();
			
			return new UserSession(Long.parseLong(userId));
		} catch (JwtException e) {
			throw new UnAuthorized();
		}
	}
}
