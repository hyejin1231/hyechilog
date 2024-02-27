package com.myrou.hyechilog.config.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User
{
	private final Long userId;
	public UserPrincipal(com.myrou.hyechilog.api.domain.blog.User user)
	{
		super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
		this.userId = user.getId();
	}
}
