package com.gestaodeloja.authenticator.service.validator;

import com.gestaodeloja.authenticator.exception.UserJaExisteException;
import com.gestaodeloja.authenticator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    public void validaSeUserJaExistePeloNome(String userName) {
        if (userRepository.existsByUserName(userName)) {
            throw new UserJaExisteException();
        }
    }
}
