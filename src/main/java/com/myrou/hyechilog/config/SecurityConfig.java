package com.myrou.hyechilog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.config.security.CustomUserDetailService;

/**
 * Spring Security 관련 설정은 여기서 함 !
 */
@Configuration
@EnableWebSecurity

public class SecurityConfig
{
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
								authorize.requestMatchers( "/auth/login").permitAll()
										.requestMatchers( "/auth/sign").permitAll()
										.requestMatchers("/admin")
											.access(new WebExpressionAuthorizationManager("hasRole('ADMIN') AND hasAuthority('WRITE')")) // '관리자' 역할이면서 '쓰기' 권한이 있는 사람만 관리자 페이지 접근 가능
										.anyRequest().authenticated()
				)
				.formLogin(
						form ->
								form.loginPage("/auth/login")
										.loginProcessingUrl("/auth/login")
										.usernameParameter("username")
										.passwordParameter("password")
										.defaultSuccessUrl("/")
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
	
	// InMemory 방식
//	@Bean
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
