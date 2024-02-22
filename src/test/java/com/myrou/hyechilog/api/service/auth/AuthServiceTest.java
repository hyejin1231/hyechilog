package com.myrou.hyechilog.api.service.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.exception.InvalidLoginInformation;
import com.myrou.hyechilog.api.exception.UnAuthorized;
import com.myrou.hyechilog.api.repository.user.SessionRepository;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.api.service.auth.response.AuthResponse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest
{
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@BeforeEach
	void setUp()
	{
		sessionRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	@DisplayName("사용자를 미리 한명 저장해둔 다음, 로그인 요청 시 로그인 성공 후 세션 생성해서 세션 리턴한다.")
	@Test
	void loginSuccessTest() {
	    // given
		User user = User.builder().email("hyechii@gmail.com").password("1231")
				.name("hyechii").build();
		User saveUser = userRepository.save(user);
		
		LoginRequest request = LoginRequest.builder().email("hyechii@gmail.com")
				.password("1231")
				.build();
		
		// when
//		AuthResponse authResponse = authService.login(request);
		Long userId = authService.login(request);
		
		// then
//		assertThat(authResponse).isNotNull();
		assertThat(userId).isEqualTo(saveUser.getId());
	}
	
	@DisplayName("사용자를 미리 한명 저장해둔 다음, 가입되지 않은 사용자로 로그인 요청 시 에러 발생한다.")
	@Test
	void loginFailTest() {
		// given
		User user = User.builder().email("hyechii@gmail.com").password("1231")
				.name("hyechii").build();
		userRepository.save(user);
		
		LoginRequest request = LoginRequest.builder().email("hyechii222@gmail.com")
				.password("123100")
				.build();
		
		// when.. then
		Assertions.assertThatThrownBy(() -> {authService.login(request);})
				.isInstanceOf(InvalidLoginInformation.class)
				.hasMessage("아이디/비밀번호를 올바르게 입력하세요.");
		
	}
	
}