package com.fitu.benefitu.global.config;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
public class JwtConfiguration {
    @Bean
    public KeyPair keyPair() {
        try {
            var keyPair = KeyPairGenerator.getInstance("RSA");
            keyPair.initialize(2048);
            return keyPair.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public com.nimbusds.jose.jwk.RSAKey rsaKey(KeyPair keyPair) {
        return new com.nimbusds.jose.jwk.RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(com.nimbusds.jose.jwk.RSAKey rsaKey) {
        var jwkSet = new JWKSet(rsaKey);

        var jwkSource = new JWKSource() {

            @Override
            public List<JWK> get(JWKSelector jwkSelector, SecurityContext securityContext) throws KeySourceException {
                return jwkSelector.select(jwkSet);
            }
        };
        return jwkSource;
    }

    @Bean
    public JwtDecoder jwtDecoder(com.nimbusds.jose.jwk.RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAKey rsaKey) {
        return new NimbusJwtEncoder(jwkSource(rsaKey));
    }
}
