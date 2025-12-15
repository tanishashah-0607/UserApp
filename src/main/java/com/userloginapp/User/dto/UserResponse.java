package com.userloginapp.User.dto;
import com.userloginapp.User.entity.UserEntity;

public record UserResponse(
        Long id,
        String fullName,
        String email
) {
    public static UserResponse from(UserEntity u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail());
    }
}
