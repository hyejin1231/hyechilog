package com.myrou.hyechilog.api.controller.auth;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.controller.blog.ApiResponse;
import com.myrou.hyechilog.api.service.auth.AuthService;
import com.myrou.hyechilog.api.service.auth.response.AuthResponse;
import com.myrou.hyechilog.config.AppConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
	private final AuthService authService;
	
	private final AppConfig appConfig;
	
//	private static final String KEY = "tESFRxlqGAmAiPkktb+gvKfvIRh2JpLGch2xGJtWBUg=";
	
	/*
	@PostMapping("/auth/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest)
	{
		// 1) json 으로 id, password 받기
		log.info("login >>> {} ", loginRequest);
		
		// 2) DB 에서 조회
		AuthResponse authResponse = authService.login(loginRequest);
		
		ResponseCookie cookie = ResponseCookie.from("SESSION",
													authResponse.getAccessToken())
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(Duration.ofDays(30))
				.sameSite("Strict")
				.build();
		log.info(">>> cookie = {}", cookie);
		
		// 3) 토큰 발급 및 리턴 ?
		return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
	}
	 */
	
	@PostMapping("/auth/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest)
	{
		// 2) DB 에서 조회
		Long userId = authService.login(loginRequest);
		
		SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getSecretKey());
		
		String jws = Jwts.builder()
				.subject(String.valueOf(userId))
				.signWith(secretKey)
				.setIssuedAt(new Date()) // -> 새로 생성
				.compact();
		
		ResponseCookie cookie = ResponseCookie.from("SESSION", jws)
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(Duration.ofDays(30))
				.sameSite("Strict")
				.build();
		log.info(">>> cookie = {}", cookie);
		
		// 3) 토큰 발급 및 리턴 ?
		return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
	}
}
