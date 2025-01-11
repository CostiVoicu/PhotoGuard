package com.example.rest_api.database.usersdb.repository;

import com.example.rest_api.database.usersdb.model.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

}
