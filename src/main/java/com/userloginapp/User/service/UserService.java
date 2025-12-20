package com.userloginapp.User.service;

import com.userloginapp.User.entity.UserEntity;
import com.userloginapp.User.exception.BadRequestException;
import com.userloginapp.User.exception.ResourceNotFoundException;
import com.userloginapp.User.exception.UnauthorizedException;
import com.userloginapp.User.repo.UserRepo;
import com.userloginapp.User.dto.RegisterRequest;
import com.userloginapp.User.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final BCryptPasswordEncoder passwordEncoder;

    // Register new user
    public UserEntity register(RegisterRequest req) {
        String email = norm(req.email());
        if (repo.existsByEmail(email)) {
            throw new BadRequestException("Email already in use");
        }

        UserEntity u = UserEntity.builder()
                .fullName(req.fullName())
                .email(email)
                .passwordHash(passwordEncoder.encode(req.password())) // hash password
                .deleted(false)
                .role(req.role())
                .build();

        return repo.save(u);
    }

    // Login
    public UserEntity login(LoginRequest req) {
        String email = norm(req.email());
        UserEntity u = repo.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");

        }
        return u;
    }

    // Fetch active user
    public UserEntity getActiveById(Long id) {
        return repo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    // Update name
    public UserEntity updateName(String email, String fullName) {

        UserEntity user = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isDeleted()) {
            throw new RuntimeException("User is deleted");
        }
        user.setFullName(fullName);
        return repo.save(user);
    }

    // Soft delete
    public void softDelete(Long id) {
        UserEntity u = getActiveById(id);
        u.setDeleted(true);
        repo.save(u);
    }

    // Hard delete
    public void hardDelete(Long id) {
        UserEntity u = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        repo.delete(u);
    }

    private String norm(String email) {
        return email == null ? null : email.toLowerCase().trim();
    }
}
