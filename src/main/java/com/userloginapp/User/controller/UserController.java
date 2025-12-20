package com.userloginapp.User.controller;

import com.userloginapp.User.config.JwtUtil;
import com.userloginapp.User.dto.*;
import com.userloginapp.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
        String token = jwtUtil.generateToken(user.getEmail(),user.getRole());
        return new AuthResponse(token);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return UserResponse.from(service.getActiveById(id));
    }

//    @PreAuthorize("hasRole('USER')")
//    @PatchMapping("/{id}/name")
//    public UserResponse updateName(@PathVariable Long id, @RequestParam String fullName) {
//        return UserResponse.from(service.updateName(id, fullName));
//    }
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/name")
    public UserResponse updateName(
            @RequestBody UpdateNameRequest req,
            Authentication authentication
    ) {
        return UserResponse.from(
                service.updateName(authentication.getName(), req.fullName())
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        service.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            Authentication authentication
    ) {
        service.changePassword(authentication.getName(), req);
        return ResponseEntity.ok("Password changed successfully");
    }



}