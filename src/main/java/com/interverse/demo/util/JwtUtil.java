package com.interverse.demo.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.KeyAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;

@Component
public class JwtUtil {

	@Value("${jwtt.secret}")
	private String secret;

	@Value("${jwtt.token.expiration}")
	private long expiration;
	
	// JWT Encrypted with a Password
	Password password = Keys.password("edawizF0cyw1KWcZHzcwzDFvQYOpy0vSw4obselq89Y=".toCharArray());

	// Choose the desired PBES2 key derivation algorithm:
	KeyAlgorithm<Password, Password> alg = Jwts.KEY.PBES2_HS512_A256KW;

	// Choose the Encryption Algorithm used to encrypt the payload:
	AeadAlgorithm enc = Jwts.ENC.A256GCM;

	public String generateEncryptedJwt(String loggedInUserData) {
		return Jwts.builder()
				.subject(loggedInUserData)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 86400000))
				.encryptWith(password, alg, enc)
				.compact();
	}

	public String validateJWT(String token) {
		try {
			return Jwts.parser()
					.decryptWith(password)
					.build()
					.parseEncryptedClaims(token)
					.getPayload()
					.getSubject();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

}
