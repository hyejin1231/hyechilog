package com.myrou.hyechilog.annotation;

import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class BlogMockSecurityFactory implements WithSecurityContextFactory<BlogWithMockUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(BlogWithMockUser annotation) {
        User user = User.builder().email(annotation.email()).name(annotation.username()).password(annotation.password()).build();
        userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(annotation.role());

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(principal, user.getPassword(), List.of(authority));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(token);

        return securityContext;
    }
}
