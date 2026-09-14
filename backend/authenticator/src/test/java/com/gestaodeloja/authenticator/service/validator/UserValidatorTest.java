package com.gestaodeloja.authenticator.service.validator;

import com.gestaodeloja.authenticator.exception.UserJaExisteException;
import com.gestaodeloja.authenticator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;


    @Test
    void deveRetornarFalseSeUsuarioNaoExistirPeloNome() {
        String userName = "usernome.inexistente";
        when(userRepository.existsByUserName(userName)).thenReturn(false);

        assertThatCode(() -> userValidator.validaSeUserJaExistePeloNome(userName))
                .doesNotThrowAnyException();

        verify(userRepository).existsByUserName(userName);
    }

    @Test
    void deveLancarTrueQuandoUsuarioJaExistirPeloNome() {
        String userName = "joao.pedro";
        when(userRepository.existsByUserName(userName)).thenReturn(true);

        assertThatCode(() -> userValidator.validaSeUserJaExistePeloNome(userName))
                .isInstanceOf(UserJaExisteException.class)
                .hasMessage("O usuario informado ja existe.");

        verify(userRepository).existsByUserName(userName);
    }
}
