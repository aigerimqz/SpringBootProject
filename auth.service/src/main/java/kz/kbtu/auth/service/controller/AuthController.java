package kz.kbtu.auth.service.controller;

import jakarta.validation.Valid;
import kz.kbtu.auth.service.dto.AuthDTO.AuthResponse;
import kz.kbtu.auth.service.dto.AuthDTO.LoginRequest;
import kz.kbtu.auth.service.dto.AuthDTO.RegisterRequest;
import kz.kbtu.auth.service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}