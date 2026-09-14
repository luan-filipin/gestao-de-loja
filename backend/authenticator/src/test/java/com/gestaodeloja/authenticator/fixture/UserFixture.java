package com.gestaodeloja.authenticator.fixture;

import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.domain.enums.UserRole;

import java.time.LocalDateTime;

public class UserFixture {

    public static User criaUser(
            Long id,
            String userName,
            String password,
            UserRole role,
            boolean enabled,
            LocalDateTime created,
            LocalDateTime updated) {
        return User.builder()
                .id(id)
                .userName(userName)
                .password(password)
                .role(role)
                .enabled(enabled)
                .created(created)
                .updated(updated)
                .build();
    }
}
