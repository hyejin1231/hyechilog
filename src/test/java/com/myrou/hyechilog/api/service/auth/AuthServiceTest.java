package com.myrou.hyechilog.api.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.controller.auth.request.SignRequest;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.exception.AlreadyExistsException;
import com.myrou.hyechilog.api.exception.InvalidLoginInformation;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.api.service.auth.response.SignResponse;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest
{
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp()
	{
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
		assertThatThrownBy(() -> {authService.login(request);})
				.isInstanceOf(InvalidLoginInformation.class)
				.hasMessage("아이디/비밀번호를 올바르게 입력하세요.");
		
	}
	
	
//	@DisplayName("회원가입 성공 테스트")
//	@Test
	void sign() {
	    // given
		SignRequest request = SignRequest.builder().email("hyechilog@gmail.com")
				.name("hyechii")
				.password("1231").build();
		
		// when
		SignResponse response = authService.sign(request);
		
		// then
		assertThat(userRepository.count()).isEqualTo(1);
		assertThat(response.getEmail()).isEqualTo("hyechilog@gmail.com");
		
		User user = userRepository.findAll().iterator().next();
		assertThat(user.getPassword()).isNotEqualTo("1231");
		assertThat(user.getPassword()).isNotEmpty();
		
	}
	
	@DisplayName("회원가입 중복 이메일 가입 실패  테스트")
	@Test
	void signWithDupEmail() {
		// given
		User preUser = User.builder().email("hyechilog@gmail.com").password("1231")
				.name("heychii").build();
		userRepository.save(preUser);
		
		SignRequest request = SignRequest.builder().email("hyechilog@gmail.com")
				.name("hyechii")
				.password("1231").build();
		
		// when
		
		assertThatThrownBy(() -> authService.sign(request))
				.isInstanceOf(
						AlreadyExistsException.class)
				.hasMessage("이미 가입된 이메일입니다.");
	}
	
}