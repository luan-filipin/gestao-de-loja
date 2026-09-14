package com.gestaodeloja.authenticator;

import com.gestaodeloja.authenticator.config.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PostgresTestContainer.class)
class AuthenticatorApplicationTests {

    @Test
    void contextLoads() {
    }

}
