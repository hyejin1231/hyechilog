package com.myrou.hyechilog.config;

import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.exception.BlogNotFound;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class HyechilogPermissionEvaluator implements PermissionEvaluator {

    private final BlogRepository blogRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Blog blog = blogRepository.findById((Long) targetId).orElseThrow(BlogNotFound::new);

        if (!blog.getUserId().equals(principal.getUserId())) {
            log.error("[인증오류] 해당 사용자가 작성한 글이 아닙니다. targetId={}", targetId);
            return false;
        }

        return true;
    }
}
