package com.myrou.hyechilog.config.security;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 역할 : 관리자, 매니저, 사용자
 * 권한 : 쓰기, 읽기, 사용자 정지 권한 등
 */
@Getter
public class UserPrincipal extends User
{
	private final Long userId;
	public UserPrincipal(com.myrou.hyechilog.api.domain.blog.User user)
	{
		super(user.getEmail(), user.getPassword(),
			  List.of(new SimpleGrantedAuthority("ROLE_ADMIN") // ROLE 을 표기하면 역할
					 /* , new SimpleGrantedAuthority("WRITE")*/)); // ROLE 표기 안하면 권한
		this.userId = user.getId();
	}
}
