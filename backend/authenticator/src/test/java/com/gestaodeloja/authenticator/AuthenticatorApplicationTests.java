package com.gestaodeloja.authenticator;

import com.gestaodeloja.authenticator.config.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Import(PostgresTestContainer.class)
class AuthenticatorApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRunSpringApplication() {
        String[] args = new String[]{};

        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            AuthenticatorApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(AuthenticatorApplication.class, args));
        }
    }
}
