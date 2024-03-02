package com.myrou.hyechilog.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService
{
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " 을 찾을 수 없습니다."));// username == id == 우리 서비스는 email
		
		return new UserPrincipal(user);
	}
}
