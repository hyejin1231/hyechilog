package com.myrou.hyechilog.support.crypto;

public interface PasswordEncoder {
    String encrypt(String rawPassword);

    boolean matches(String rawPassword, String encryptedPassword);
}
