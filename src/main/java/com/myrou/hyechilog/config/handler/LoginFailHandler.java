package com.myrou.hyechilog.config.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.blog.ApiResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler
{
	private final ObjectMapper objectMapper;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException
	{
		log.error("[인증오류] 아이디 또는 비밀번호가 올바르지 않습니다.");
		
		ApiResponse<String> apiResponse = ApiResponse.of(HttpStatus.BAD_REQUEST,
														 "아이디 또는 비밀번호가 올바르지 않습니다.");
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		
		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}
