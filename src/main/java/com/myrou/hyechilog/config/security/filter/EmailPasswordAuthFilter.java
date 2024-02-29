package com.myrou.hyechilog.config.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter
{
	private final ObjectMapper objectMapper;
	
	public EmailPasswordAuthFilter(String defaultFilterProcessesUrl,
			ObjectMapper objectMapper)
	{
		super(defaultFilterProcessesUrl);
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException
	{
		EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(emailPassword.getEmail(), emailPassword.getPassword());
		
		token.setDetails(this.authenticationDetailsSource.buildDetails(request));
		
		return this.getAuthenticationManager().authenticate(token);
	}
}
