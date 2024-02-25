package com.myrou.hyechilog.api.service.auth;

import java.util.Optional;

import com.myrou.hyechilog.support.crypto.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.controller.auth.request.SignRequest;
import com.myrou.hyechilog.api.domain.blog.Session;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.exception.AlreadyExistsException;
import com.myrou.hyechilog.api.exception.InvalidLoginInformation;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.api.service.auth.response.AuthResponse;
import com.myrou.hyechilog.api.service.auth.response.SignResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService
{
	private final UserRepository userRepository;
	
	@Transactional
	public Long login(LoginRequest loginRequest)
	{
		// 1) 로그인 처리
//		User user = userRepository.findByEmailAndPassword(
//						loginRequest.getEmail(), loginRequest.getPassword())
//				.orElseThrow(InvalidLoginInformation::new);
		
		// 2) 세션 토큰 발급
//		Session session = user.addSession();
		
//		return new AuthResponse(session.getAccessToken());

		// 1) 이메일을 통해 user 조회
		User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(InvalidLoginInformation::new);

		// 2) 비밀번호 암호화
		PasswordEncoder passwordEncoder = new PasswordEncoder();
		boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
		if (!matches) {
			throw new InvalidLoginInformation();
		}

		return user.getId();
	}
	
	public SignResponse sign(SignRequest signRequest)
	{
		// 1) 이메일 중복 체크
		Optional<User> byEmail = userRepository.findByEmail(signRequest.getEmail());
		if (byEmail.isPresent()) {
			throw new AlreadyExistsException();
		}
//		SCryptPasswordEncoder passwordEncoder = new SCryptPasswordEncoder(16, 8,
//																		  1, 32,
//																		  64);
		// 2) 비밀번호 암호화
		PasswordEncoder passwordEncoder = new PasswordEncoder();
		String password = signRequest.getPassword();
		signRequest.setPassword(passwordEncoder.encrypt(password));
		
		User entity = SignRequest.toEntity(signRequest);
		
		User save = userRepository.save(entity);
		return new SignResponse(save.getEmail());
	}
}
