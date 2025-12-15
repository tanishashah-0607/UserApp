package com.userloginapp.User.controller;

import com.userloginapp.User.config.JwtUtil;
import com.userloginapp.User.dto.AuthResponse;
import com.userloginapp.User.dto.LoginRequest;
import com.userloginapp.User.dto.RegisterRequest;
import com.userloginapp.User.dto.UserResponse;
import com.userloginapp.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest req) {
        return UserResponse.from(service.register(req));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        var user = service.login(req);
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }


    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return UserResponse.from(service.getActiveById(id));
    }

    @PatchMapping("/{id}/name")
    public UserResponse updateName(@PathVariable Long id, @RequestParam String fullName) {
        return UserResponse.from(service.updateName(id, fullName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        service.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}