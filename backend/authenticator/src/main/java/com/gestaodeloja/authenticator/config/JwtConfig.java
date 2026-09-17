package com.gestaodeloja.authenticator.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${jwt.private-key}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key}")
    private Resource publicKeyResource;

    @Bean
    public RSAPrivateKey privateKey() {
        try {
            String key = new String(privateKeyResource.getInputStream().readAllBytes());

            key = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = java.util.Base64.getDecoder().decode(key);

            java.security.spec.PKCS8EncodedKeySpec keySpec =
                    new java.security.spec.PKCS8EncodedKeySpec(keyBytes);

            java.security.KeyFactory keyFactory =
                    java.security.KeyFactory.getInstance("RSA");

            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        } catch (Exception e) {
            throw new IllegalStateException("Erro ao carregar chave privada", e);
        }
    }

    @Bean
    public RSAPublicKey publicKey() {
        try {
            String key = new String(publicKeyResource.getInputStream().readAllBytes());

            key = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);

            X509EncodedKeySpec keySpec =
                    new X509EncodedKeySpec(keyBytes);

            KeyFactory keyFactory =
                    KeyFactory.getInstance("RSA");

            return (RSAPublicKey) keyFactory.generatePublic(keySpec);

        } catch (Exception e) {
            throw new IllegalStateException("Erro ao carregar chave pública", e);
        }
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAPrivateKey privateKey, RSAPublicKey publicKey) {

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

}
