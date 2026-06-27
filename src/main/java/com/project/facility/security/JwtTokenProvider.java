package com.project.facility.security;

import com.project.facility.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 생성과 검증을 담당하는 클래스
@Component
public class JwtTokenProvider {

    // 실제 운영에서는 코드에 직접 작성하지 않고 환경변수로 관리해야 함
    private final String secretKey = "facility-platform-secret-key-for-jwt-token-2026";

    // 토큰 유효 시간: 1시간
    private final long tokenValidityTime = 1000L * 60 * 60;

    // JWT 서명에 사용할 SecretKey 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    // JWT 생성
    public String createToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(
                now.getTime() + tokenValidityTime
        );

        return Jwts.builder()
                // 토큰의 주체
                .subject(user.getEmail())

                // 토큰에 담을 사용자 정보
                .claim("userId", user.getId())
                .claim("role", user.getRole().toString())

                // 발급 시간
                .issuedAt(now)

                // 만료 시간
                .expiration(expiryDate)

                // SecretKey로 서명
                .signWith(getSigningKey())

                // 문자열 JWT 생성
                .compact();
    }
    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            // JWT 라이브러리에게 JWT하나 검사할게 뜻
            Jwts.parser()
                    // signature 검증
                    .verifyWith(getSigningKey())
                    // build패턴을 사용하는것
                    .build()
                    // 실제 검증
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    // JWT에서 이메일 추출
    public String getEmailFromToken(String token) {
        // Claimes는 payload(전송되는 데이터 자체)
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
    // JWT에서 권한 추출
    public String getRoleFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }
}