package com.userloginapp.User.repo;

import com.userloginapp.User.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);
    Optional<UserEntity> findByIdAndDeletedFalse(Long id);
}
