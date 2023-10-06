package com.pokemoney.userservice.repository;

import com.pokemoney.userservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * This is a repository of user role.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
