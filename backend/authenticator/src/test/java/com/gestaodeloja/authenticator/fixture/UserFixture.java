package com.gestaodeloja.authenticator.fixture;

import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.domain.enums.UserRole;

public class UserFixture {

    public static User criaUser(
            Long id,
            String userName,
            String password,
            String role) {
        return User.builder()
                .id(id)
                .userName(userName)
                .password(password)
                .role(UserRole.valueOf(role))
                .build();
    }
}
