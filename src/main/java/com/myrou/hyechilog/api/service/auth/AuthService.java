package com.myrou.hyechilog.api.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.domain.blog.Session;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.exception.InvalidLoginInformation;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.api.service.auth.response.AuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService
{
	private final UserRepository userRepository;
	
	@Transactional
	public AuthResponse login(LoginRequest loginRequest)
	{
		// 1) 로그인 처리
		User user = userRepository.findByEmailAndPassword(
						loginRequest.getEmail(), loginRequest.getPassword())
				.orElseThrow(InvalidLoginInformation::new);
		
		// 2) 세션 토큰 발급
		Session session = user.addSession();
		
		return new AuthResponse(session.getAccessToken());
	}
}
