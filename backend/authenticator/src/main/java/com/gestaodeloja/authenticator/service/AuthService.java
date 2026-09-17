package com.gestaodeloja.authenticator.service;

import com.gestaodeloja.authenticator.dto.request.LoginRequestDto;
import com.gestaodeloja.authenticator.dto.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDto authenticate(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.userName(),
                        dto.password())
        );

        return new LoginResponseDto(jwtService.generateToken(authentication));
    }
}
