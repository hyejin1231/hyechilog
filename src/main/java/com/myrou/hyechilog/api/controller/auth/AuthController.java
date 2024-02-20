package com.myrou.hyechilog.api.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.exception.InvalidLoginInformation;
import com.myrou.hyechilog.api.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController
{
	private final UserRepository userRepository;
	
	@PostMapping("/auth/login")
	public User login(@RequestBody LoginRequest loginRequest)
	{
		// 1) json 으로 id, password 받기
		log.info("login >>> {} ", loginRequest);
		
		// 2) DB 에서 조회
		User user = userRepository.findByEmailAndPassword(
						loginRequest.getEmail(), loginRequest.getPassword())
				.orElseThrow(InvalidLoginInformation::new);
		
		// 3) 토큰 발급 및 리턴 ?
		return user;
	}
}
