package com.gestaodeloja.authenticator.domain.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserRoleTest {

    @Test
    void deveRetornarValorParaUser() {
        assertThat("user").isEqualTo(UserRole.USER.getRole());
    }

    @Test
    void deveRetornarValorParaAdmin() {
        assertThat("admin").isEqualTo(UserRole.ADMIN.getRole());
    }
}
