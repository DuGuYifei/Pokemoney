package com.pokemoney.userservice.repository;

import com.pokemoney.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is a repository of t_users table.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

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
     * @return User.
     */
    User findByUsername(String username);

    /**
     * Find user by email.
     *
     * @param email Email.
     * @return User.
     */
    User findByEmail(String email);
}
