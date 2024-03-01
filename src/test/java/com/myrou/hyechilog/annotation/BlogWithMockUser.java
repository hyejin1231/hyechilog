package com.myrou.hyechilog.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithSecurityContext(factory = BlogMockSecurityFactory.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface BlogWithMockUser {
    String username() default "hyechi";
    String email() default "hyechilog@gmail.com";
    String password() default "1231";

    String role() default "ROLE_ADMIN";
}
