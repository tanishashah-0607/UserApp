package com.userloginapp.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNameRequest(
        @NotBlank
        @Size(min = 2, max = 80)
        String fullName
) {}
