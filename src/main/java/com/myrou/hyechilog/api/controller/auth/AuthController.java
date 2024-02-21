package com.myrou.hyechilog.api.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.controller.blog.ApiResponse;
import com.myrou.hyechilog.api.service.auth.AuthService;
import com.myrou.hyechilog.api.service.auth.response.AuthResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
	private final AuthService authService;
	
	@PostMapping("/auth/login")
	public ApiResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest)
	{
		// 1) json 으로 id, password 받기
		log.info("login >>> {} ", loginRequest);
		
		// 2) DB 에서 조회
		AuthResponse authResponse = authService.login(loginRequest);
		
		// 3) 토큰 발급 및 리턴 ?
		return ApiResponse.ok(authResponse);
	}
}
