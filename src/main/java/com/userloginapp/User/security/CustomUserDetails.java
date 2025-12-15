package com.userloginapp.User.security;

import com.userloginapp.User.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // email = username
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public boolean isEnabled() {
        return !user.isDeleted();
    }

    @Override
    public java.util.Collection getAuthorities() {
        return Collections.emptyList(); // no roles now
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
}
