package com.fitu.benefitu.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitu.benefitu.global.config.jwt.JsonLoginSuccessHandler;
import com.fitu.benefitu.global.config.jwt.JwtFilter;
import com.fitu.benefitu.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JsonLoginSuccessHandler jsonLoginSuccessHandler, JwtFilter jwtFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/login"    // 최초 로그인 시 열어둘 주소
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // 예외 처리
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            // 실패 응답 형식
                            ApiResponse<Void> apiResponse = new ApiResponse<>(
                                    false,
                                    "AUTH_401",
                                    "인증되지 않는 사용자입니다. 유효한 토큰을 포함해주세요.",
                                    null
                            );
                            // ObjectMapper를 통해 "ApiResponse -> JSON" 변환 후 응답
                            String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
                            response.getWriter().write(jsonResponse);
                        }))
                // 세션 정책 -> 무상태성
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 토큰 반환하도록 설정
                .formLogin(form -> form.successHandler(jsonLoginSuccessHandler))
                // 커스텀 JWT 필터 등록
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 실제 사용
        // return new BCryptPasswordEncoder();

        // 테스트용
        return NoOpPasswordEncoder.getInstance();
    }
}
