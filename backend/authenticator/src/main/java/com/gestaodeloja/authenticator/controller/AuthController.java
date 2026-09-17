package com.gestaodeloja.authenticator.controller;

import com.gestaodeloja.authenticator.dto.request.LoginRequestDto;
import com.gestaodeloja.authenticator.dto.response.LoginResponseDto;
import com.gestaodeloja.authenticator.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(dto));
    }
}
