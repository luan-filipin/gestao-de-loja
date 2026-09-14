package com.gestaodeloja.authenticator.service;

import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.domain.enums.UserRole;
import com.gestaodeloja.authenticator.fixture.UserFixture;
import com.gestaodeloja.authenticator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
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
    }

    @Test
    void deveLancarExceptionAoBuscarUsuarioPeloUserNameInexistente() {
        when(userRepository.findByUserName("joao.pedro")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.loadUserByUsername("joao.pedro"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuario não encontrado: " + "joao.pedro");
    }
}
