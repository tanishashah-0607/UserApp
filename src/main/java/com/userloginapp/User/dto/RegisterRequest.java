package com.userloginapp.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(min = 2, max = 80)
        String fullName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6, max = 100)
        String password,

        @NotNull
        String role   // USER or ADMIN
) { }
