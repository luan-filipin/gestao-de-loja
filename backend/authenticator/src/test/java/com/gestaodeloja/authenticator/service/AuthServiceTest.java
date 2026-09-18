package com.gestaodeloja.authenticator.service;

import com.gestaodeloja.authenticator.config.JwtConfig;
import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.domain.enums.UserRole;
import com.gestaodeloja.authenticator.dto.request.LoginRequestDto;
import com.gestaodeloja.authenticator.dto.response.LoginResponseDto;
import com.gestaodeloja.authenticator.fixture.UserFixture;
import com.gestaodeloja.authenticator.mapper.UserMapper;
import com.gestaodeloja.authenticator.repository.UserRepository;
import com.gestaodeloja.authenticator.service.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.lang.reflect.Field;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    private AuthService authService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() throws Exception {

        UserValidator userValidator = new UserValidator(userRepository);
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        passwordEncoder = new BCryptPasswordEncoder();

        UserService userService = new UserService(
                userRepository,
                passwordEncoder,
                userValidator,
                userMapper
        );

        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager =
                new ProviderManager(authenticationProvider);

        JwtConfig jwtConfig = new JwtConfig();

        Field privateKeyField = JwtConfig.class.getDeclaredField("privateKeyResource");
        privateKeyField.setAccessible(true);
        privateKeyField.set(jwtConfig, new ClassPathResource("keys/private.pem"));

        Field publicKeyField = JwtConfig.class.getDeclaredField("publicKeyResource");
        publicKeyField.setAccessible(true);
        publicKeyField.set(jwtConfig, new ClassPathResource("keys/public.pem"));

        RSAPrivateKey privateKey = jwtConfig.privateKey();
        RSAPublicKey publicKey = jwtConfig.publicKey();

        JwtEncoder jwtEncoder = jwtConfig.jwtEncoder(privateKey, publicKey);

        JwtService jwtService = new JwtService(jwtEncoder);

        authService = new AuthService(
                authenticationManager,
                jwtService
        );
    }

    @Test
    void deveAutenticarUsuarioComSucesso() {

        LoginRequestDto dtoEntrada = new LoginRequestDto("joao.pedro", "123456");
        User usuario = UserFixture.criaUser(
                1L,
                "joao.pedro",
                passwordEncoder.encode("123456"),
                UserRole.USER,
                true,
                LocalDateTime.of(2026, 9, 14, 1, 1, 1),
                null);

        when(userRepository.findByUserName("joao.pedro")).thenReturn(Optional.of(usuario));

        LoginResponseDto result = authService.authenticate(dtoEntrada);

        assertThat(result).isNotNull();
        assertThat(result.token()).isNotBlank();

        verify(userRepository).findByUserName("joao.pedro");
    }

    @Test
    void deveFalharAoAutenticarUsuarioComSenhaIncorreta() {

        LoginRequestDto dtoEntrada = new LoginRequestDto("joao.pedro", "123456");
        User usuario = UserFixture.criaUser(
                1L,
                "joao.pedro",
                passwordEncoder.encode("789456"),
                UserRole.USER,
                true,
                LocalDateTime.of(2026, 9, 14, 1, 1, 1),
                null);

        when(userRepository.findByUserName("joao.pedro")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> authService.authenticate(dtoEntrada))
                .isInstanceOf(BadCredentialsException.class);

        verify(userRepository).findByUserName("joao.pedro");

    }

}
