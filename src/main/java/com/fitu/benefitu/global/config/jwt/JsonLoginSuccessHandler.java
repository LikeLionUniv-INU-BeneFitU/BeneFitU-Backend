package com.fitu.benefitu.global.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JsonLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtEncoder jwtEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();   // JSON 변환 사용

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Instant now = Instant.now();

        // 토큰 만료 기간 : 7 일
        long expiry = 3600L * 24 * 7;

        // JWT payload
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", "USER")
                .build();

        String accessToken = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> tokenResponse = new HashMap<>();
        // Access Token
        tokenResponse.put("accessToken", accessToken);
        // 토큰 타입 : Bearer
        tokenResponse.put("tokenType", "Bearer");
        // 유효 가간 : 초 단위
        tokenResponse.put("expiresIn", expiry);

        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
    }
}
