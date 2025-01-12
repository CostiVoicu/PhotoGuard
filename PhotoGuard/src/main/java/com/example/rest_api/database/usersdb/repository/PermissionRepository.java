package com.example.rest_api.database.usersdb.repository;

import com.example.rest_api.database.usersdb.model.PermissionEntity;
import com.example.rest_api.database.usersdb.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional("primaryTransactionManager")
@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    @Query("SELECT p FROM PermissionEntity p " +
            "WHERE p.httpMethod || '/' || p.url NOT IN ('GET/**', 'POST/**', 'DELETE/**', 'PATCH/**', 'GET/home')")
    List<PermissionEntity> findAllAlbumsPermissions();
}
