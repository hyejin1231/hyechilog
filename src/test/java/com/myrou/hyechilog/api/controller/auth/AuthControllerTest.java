package com.myrou.hyechilog.api.controller.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.auth.request.LoginRequest;
import com.myrou.hyechilog.api.domain.blog.Session;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.repository.user.SessionRepository;
import com.myrou.hyechilog.api.repository.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest
{
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp()
	{
		userRepository.deleteAll();
	}
	
	@DisplayName("User 데이터를 저장한 후 로그인 요청하면 정상 로그인 처리된다.")
	@Test
	void login() throws Exception
	{
	    // given
		User user = User.builder().email("hyechii@gmail.com")
				.password("1231").name("hyechii").build();
		User saveUser = userRepository.save(user);
		
		LoginRequest request = LoginRequest.builder().email("hyechii@gmail.com")
				.password("1231")
				.build();
		
		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(
										request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

	@Transactional
	@DisplayName("User 데이터를 저장한 후 로그인 요청하면 정상 로그인 처리 후 세션이 생성 된다.")
	@Test
	void checkSessionAfterLogin() throws Exception
	{
		// given
		User user = User.builder().email("hyechii@gmail.com")
				.password("1231").name("hyechii").build();
		User saveUser = userRepository.save(user);
		
		LoginRequest request = LoginRequest.builder().email("hyechii@gmail.com")
				.password("1231")
				.build();
		
		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(
										request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").isNotEmpty())
				.andDo(MockMvcResultHandlers.print());
		
		assertThat(saveUser.getSessions().size()).isEqualTo(1);
	}
	
	@Transactional
	@DisplayName("User 데이터를 저장한 후 로그인 요청하면 정상 로그인 처리 후 세션이 생성 된다.")
	@Test
	void loginAndConnectAuthPage() throws Exception
	{
		// given
		User user = User.builder().email("hyechii@gmail.com")
				.password("1231").name("hyechii").build();
		Session session = user.addSession();
		userRepository.save(user);
		
		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", session.getAccessToken()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Transactional
	@DisplayName("User 데이터를 저장한 후 로그인 요청하면 정상 로그인 처리 후 세션이 생성 된다.")
	@Test
	void NoAuthLoginAndConnectAuthPage() throws Exception
	{
		// given
		User user = User.builder().email("hyechii@gmail.com")
				.password("1231").name("hyechii").build();
		Session session = user.addSession();
		userRepository.save(user);
		
		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", session.getAccessToken() + "-aaa"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andDo(MockMvcResultHandlers.print());
	}
	
}