package com.gestaodeloja.authenticator.service;

import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.domain.enums.UserRole;
import com.gestaodeloja.authenticator.dto.request.CreateUserRequestDto;
import com.gestaodeloja.authenticator.dto.response.CreateUserResponseDto;
import com.gestaodeloja.authenticator.exception.UserJaExisteException;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        UserValidator userValidator = new UserValidator(userRepository);
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder, userValidator, userMapper);
    }

    @Test
    void deveBuscarUsuarioPeloUserName() {

        User usuario = UserFixture.criaUser(
                1L,
                "joao.pedro",
                "123456",
                UserRole.USER,
                true,
                LocalDateTime.of(2026, 9, 14, 1, 1, 1),
                null);
        when(userRepository.findByUserName("joao.pedro")).thenReturn(Optional.of(usuario));

        UserDetails result = userService.loadUserByUsername("joao.pedro");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("joao.pedro");
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");

        verify(userRepository).findByUserName("joao.pedro");
    }

    @Test
    void deveLancarExceptionAoBuscarUsuarioPeloUserNameInexistente() {
        when(userRepository.findByUserName("joao.pedro")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.loadUserByUsername("joao.pedro"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuario não encontrado: " + "joao.pedro");

        verify(userRepository).findByUserName("joao.pedro");
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        CreateUserRequestDto dto = UserFixture.criaCreateUserRequestDto("joao.pedro", "123456", UserRole.USER);
        User user = UserFixture.criaUser(1L, "joao.pedro", "123456", UserRole.USER, true, LocalDateTime.of(2026, 9, 14, 1, 1, 1), null);
        when(userRepository.existsByUserName(dto.userName())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        CreateUserResponseDto result = userService.createUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.userName()).isEqualTo("joao.pedro");
        assertThat(result.role()).isEqualTo(UserRole.USER);
        assertThat(result.enabled()).isTrue();
        assertThat(result.created()).isNotNull();

        verify(userRepository).existsByUserName(dto.userName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarExceptionAoTentarCriarUsuarioComNomeJaExistente() {
        CreateUserRequestDto dto = UserFixture.criaCreateUserRequestDto("joao.pedro", "123456", UserRole.USER);
        when(userRepository.existsByUserName(dto.userName())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(UserJaExisteException.class)
                .hasMessage("O usuario informado ja existe.");

        verify(userRepository).existsByUserName(dto.userName());
    }
}
