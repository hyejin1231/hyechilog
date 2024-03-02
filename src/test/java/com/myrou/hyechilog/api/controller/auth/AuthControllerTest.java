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
import com.myrou.hyechilog.api.controller.auth.request.SignRequest;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.repository.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

// TODO. Controller 테스트할 때는 Mock 처리해서 해보기
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest
{
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp()
	{
		userRepository.deleteAll();
	}
	
	@DisplayName("회원가입 성공 테스트")
	@Test
	void sign() throws Exception
	{
		// given
		SignRequest request = SignRequest.builder().email("hyechilog@gmail.com")
				.name("hyechii")
				.password("1231").build();
		
		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("hyechilog@gmail.com"))
				.andDo(MockMvcResultHandlers.print());
		
	}
}