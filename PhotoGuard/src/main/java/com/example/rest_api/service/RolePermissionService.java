package com.example.rest_api.database.usersdb.service;

import com.example.rest_api.database.usersdb.model.RolePermissionEntity;
import com.example.rest_api.database.usersdb.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    // Method to save a RolePermissionEntity
    public void save(RolePermissionEntity rolePermissionEntity) {
        rolePermissionRepository.save(rolePermissionEntity);
    }

    // Optionally, add methods to retrieve and delete RolePermissionEntity instances
    // For example, if you want to remove a permission from a role:
    public void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId) {
        // Custom query or logic to delete by role and permission
        // This could be a query in the repository if needed
    }
}
