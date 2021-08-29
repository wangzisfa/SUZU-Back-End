package com.suzu.util;

import com.suzu.pojo.PlainUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil {
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("${jwt.secret}")
	private String secret;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Claims getAllClaimsFromToken(String token) {
		System.out.println(token);
		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody();
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Boolean validateToken(String token, PlainUserDTO user, boolean hasExpiration) {
		final String username = getUsernameFromToken(token);
		if (hasExpiration) {
			return (user.getUsername().equals(username) && !isTokenExpired(token));
		} else {
			return (!isTokenExpired(token));
		}
	}

	public String generateToken(PlainUserDTO user, boolean hasExpiration) {
		Map<String, Object> claims = new HashMap<>();
		if (hasExpiration) {
			return doGenerateToken(claims, user.getUsername());
		} else {
			return doGenerateTokenNonExpiration(claims, user.getUsername());
		}
	}

	private String doGenerateTokenNonExpiration(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		System.out.println(secret);
//		byte[] keyBytes = Decoders.BASE64.decode(secret);
//		Key key = Keys.hmacShaKeyFor(keyBytes);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

}
