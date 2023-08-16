package com.pokemoney.userservice.repository;

import com.pokemoney.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is a repository of t_users table.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
