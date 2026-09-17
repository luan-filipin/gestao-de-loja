package com.gestaodeloja.authenticator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(jwtEncoder);
    }

    @Test
    void deveGerarToken() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "joao.pedro",
                        null
                );
        Jwt jwt = Jwt.withTokenValue("token-jwt-gerado")
                .header("alg", "RS256")
                .claim("sub", "joao.pedro")
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        String result = jwtService.generateToken(authentication);

        assertThat(result).isEqualTo("token-jwt-gerado");

        ArgumentCaptor<JwtEncoderParameters> captor =
                ArgumentCaptor.forClass(JwtEncoderParameters.class);

        verify(jwtEncoder).encode(captor.capture());

        JwtClaimsSet claims = captor.getValue().getClaims();

        assertThat(claims.getSubject()).isEqualTo("joao.pedro");
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiresAt()).isNotNull();
    }

    @Test
    void deveFalharQuandoAuthenticationForNula() {

        assertThatThrownBy(() -> jwtService.generateToken(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void devePropagarErroQuandoEncoderFalhar() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "joao.pedro",
                        null
                );

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenThrow(new JwtEncodingException("Erro ao gerar JWT"));

        assertThatThrownBy(() -> jwtService.generateToken(authentication))
                .isInstanceOf(JwtEncodingException.class);
    }

    @Test
    void deveDefinirExpiracaoPara15Minutos() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "joao.pedro",
                        null
                );

        Jwt jwt = Jwt.withTokenValue("token-jwt-gerado")
                .header("alg", "RS256")
                .claim("sub", "joao.pedro")
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        jwtService.generateToken(authentication);

        ArgumentCaptor<JwtEncoderParameters> captor =
                ArgumentCaptor.forClass(JwtEncoderParameters.class);

        verify(jwtEncoder).encode(captor.capture());

        JwtClaimsSet claims = captor.getValue().getClaims();

        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiresAt()).isNotNull();

        long segundos =
                Duration.between(
                        claims.getIssuedAt(),
                        claims.getExpiresAt()
                ).getSeconds();

        assertThat(segundos).isEqualTo(900);
    }
}
