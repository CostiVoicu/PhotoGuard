package com.example.rest_api.database.usersdb.repository;

import com.example.rest_api.database.usersdb.model.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {
}
