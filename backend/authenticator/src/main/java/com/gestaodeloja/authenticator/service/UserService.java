package com.gestaodeloja.authenticator.service;

import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.dto.request.CreateUserRequestDto;
import com.gestaodeloja.authenticator.dto.response.CreateUserResponseDto;
import com.gestaodeloja.authenticator.mapper.UserMapper;
import com.gestaodeloja.authenticator.repository.UserRepository;
import com.gestaodeloja.authenticator.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado: " + username));
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto dto) {
        userValidator.validaSeUserJaExistePeloNome(dto.userName());
        User userEntity = userMapper.toRequestEntity(dto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userMapper.toResponseDto(userRepository.save(userEntity));
    }

}
