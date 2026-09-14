package com.gestaodeloja.authenticator.repository;

import com.gestaodeloja.authenticator.config.PostgresTestContainer;
import com.gestaodeloja.authenticator.domain.User;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestContainer.class)
@DBRider
@DBUnit(
        cacheConnection = false,
        schema = "public",
        disableSequenceFiltering = true,
        alwaysCleanBefore = true,
        alwaysCleanAfter = true)
@DataSet("datasets/users.xml")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void deveBuscarUsuarioPeloUserName() {
        Optional<User> user = userRepository.findByUserName("joao.pedro");
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("joao.pedro");
    }

    @Test
    void deveRetornarFalsoAoBuscarUsuarioPeloUserNameInexistente() {
        Optional<User> user = userRepository.findByUserName("nome.inexistente");
        assertThat(user).isEmpty();
    }

    @Test
    void deveRetornarTrueSeUserNameExistir() {
        boolean existe = userRepository.existsByUserName("joao.pedro");
        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalseSeUserNameNaoExistir() {
        boolean existe = userRepository.existsByUserName("nome.inexistente");
        assertThat(existe).isFalse();
    }
}
