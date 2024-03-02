package com.myrou.hyechilog.config.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.blog.ApiResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Http403Handler implements AccessDeniedHandler
{
	private final ObjectMapper objectMapper;
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException, ServletException
	{
		log.error("[인증 오류] 403 : 접근할 수 없습니다.");
		
		ApiResponse<String> apiResponse = ApiResponse.of(HttpStatus.FORBIDDEN, "접근할 수 없습니다.");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		
		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}
