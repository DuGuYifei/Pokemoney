package com.pokemoney.userservice.repository;

import com.pokemoney.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is a repository of user.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Check if user exists by username.
     *
     * @param username Username.
     * @return True if user exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Check if user exists by email.
     *
     * @param email Email.
     * @return True if user exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Find user by username.
     *
     * @param username Username.
     * @return UserEntity.
     */
    UserEntity findByUsername(String username);

    /**
     * Find user by email.
     *
     * @param email Email.
     * @return UserEntity.
     */
    UserEntity findByEmail(String email);
}
