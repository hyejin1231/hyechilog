package com.myrou.hyechilog.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

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
								authorize.requestMatchers("/auth/login").permitAll()
										.anyRequest().authenticated()
				)
				.csrf(AbstractHttpConfigurer::disable)
				.build();
	}
}
