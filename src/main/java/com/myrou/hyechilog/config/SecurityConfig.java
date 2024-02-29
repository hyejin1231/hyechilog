package com.myrou.hyechilog.config;

import com.myrou.hyechilog.config.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.config.handler.Http401Handler;
import com.myrou.hyechilog.config.handler.Http403Handler;
import com.myrou.hyechilog.config.handler.LoginFailHandler;
import com.myrou.hyechilog.config.security.CustomUserDetailService;
import com.myrou.hyechilog.config.security.filter.EmailPasswordAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security 관련 설정은 여기서 함 !
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
	private final ObjectMapper objectMapper;
	
	private final UserRepository userRepository;
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer()
	{
		return web -> web.ignoring().requestMatchers("/favicon.ico", "/error")
				.requestMatchers(PathRequest.toH2Console());
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		return httpSecurity
				.authorizeHttpRequests(
						authorize ->
								authorize
										.anyRequest().permitAll()
//										.requestMatchers( "/auth/login").permitAll()
//										.requestMatchers( "/auth/sign").permitAll()
//										.requestMatchers("/admin").hasRole("ADMIN")
//											.access(new WebExpressionAuthorizationManager("hasRole('ADMIN') AND hasAuthority('WRITE')")) // '관리자' 역할이면서 '쓰기' 권한이 있는 사람만 관리자 페이지 접근 가능
//										.anyRequest().authenticated()
				)
				.addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//				.formLogin( // 1) 로그인 방식 : 폼 로그인 방식
//						form ->
//								form.loginPage("/auth/login")
//										.loginProcessingUrl("/auth/login")
//										.usernameParameter("username")
//										.passwordParameter("password")
//										.defaultSuccessUrl("/")
//										.failureHandler(new LoginFailHandler(objectMapper))
//				)
				.exceptionHandling( // 시큐리티 예외 핸들링
						e ->
								e.accessDeniedHandler(new Http403Handler(objectMapper))
										.authenticationEntryPoint(new Http401Handler(objectMapper))
				)
				.rememberMe(
						rm ->
								rm.rememberMeParameter("remember")
										.alwaysRemember(false)
										.tokenValiditySeconds(2592000) // 30일 동안 저장
				)
				.csrf(AbstractHttpConfigurer::disable)
				.build();
	}
	
	@Bean
	public EmailPasswordAuthFilter emailPasswordAuthFilter()
	{
		EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
//		filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
		filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
		filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository()); // 이걸 꼭 해줘야 세션이 정상 생성 된다고 함!
		
		SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
		rememberMeServices.setAlwaysRemember(true);
		rememberMeServices.setValiditySeconds(3600 * 24 * 30);
		filter.setRememberMeServices(rememberMeServices);
		
		return filter;
	}
	
	@Bean
	public AuthenticationManager authenticationManager()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(new CustomUserDetailService(userRepository));
		provider.setPasswordEncoder(passwordEncoder());
		
		return new ProviderManager(provider);
	}
	
	
	// InMemory 방식
	//@Bean
	public UserDetailsService InMemoryUserDetailsService()
	{
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		UserDetails user = User.withUsername("hyechi").password("1231")
				.roles("ADMIN").build();
		manager.createUser(user);
		
		return manager;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
//		return NoOpPasswordEncoder.getInstance();
		return new SCryptPasswordEncoder(16, 8, 1, 32, 64);
	}
}
