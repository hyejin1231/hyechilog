package com.myrou.hyechilog;

import java.util.Base64;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class CreateSecretKey
{
	public static void main(String[] args)
	{
		SecretKey key = Jwts.SIG.HS256.key().build();
		
		byte[] encodedKey = key.getEncoded();
		String SecretKey = Base64.getEncoder().encodeToString(encodedKey);
		System.out.println("SecretKey = " + SecretKey);
	}
}
