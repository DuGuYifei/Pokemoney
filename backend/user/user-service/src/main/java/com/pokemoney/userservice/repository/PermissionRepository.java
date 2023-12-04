package com.pokemoney.userservice.repository;

import com.pokemoney.userservice.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is a repository of user permission.
 */
@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer>{
}
