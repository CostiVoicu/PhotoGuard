package com.example.rest_api.service;

import com.example.rest_api.database.usersdb.model.PermissionEntity;
import com.example.rest_api.database.usersdb.model.RoleEntity;
import com.example.rest_api.database.usersdb.model.RolePermissionEntity;
import com.example.rest_api.database.usersdb.repository.PermissionRepository;
import com.example.rest_api.database.usersdb.repository.RolePermissionRepository;
import com.example.rest_api.database.usersdb.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
    }

    public List<PermissionEntity> findAll() {
        return permissionRepository.findAll();
    }
    public List<PermissionEntity> findAllAlbumsPermissions() {
        return permissionRepository.findAllAlbumsPermissions();
    }

    @Transactional
    public void deletePermission(Long permissionId) {
        rolePermissionRepository.deleteByPermissionId(permissionId);

        permissionRepository.deleteById(permissionId);
    }

    public void save(PermissionEntity permissionEntity) {
        this.permissionRepository.save(permissionEntity);
    }

    public Optional<PermissionEntity> findById(Long id) {
        return permissionRepository.findById(id);
    }
    public List<PermissionEntity> findAllById(List<Long> ids) {
        return permissionRepository.findAllById(ids);
    }

    public boolean hasPermissionForHttpMethod(List<RoleEntity> roles, String targetHttpMethod) {
        if (roles == null || roles.isEmpty() || targetHttpMethod == null || targetHttpMethod.trim().isEmpty()) {
            return false;
        }

        // Fetch roles with permissions eagerly using the repository method
        List<RoleEntity> rolesWithPermissions = roleRepository.findRolesWithPermissionsIn(roles);

        for (RoleEntity role : rolesWithPermissions) { // Use rolesWithPermissions here
            if (role.getRolePermissions() != null && !role.getName().equals("USER")) {
                for (RolePermissionEntity rolePermission : role.getRolePermissions()) {
                    PermissionEntity permission = rolePermission.getPermission();
                    if (permission != null && permission.getHttpMethod() != null) {
                        if (permission.getHttpMethod().equalsIgnoreCase(targetHttpMethod)) {
                            return true; // Found a match!
                        }
                    }
                }
            }
        }
        return false; // No match found
    }

    public boolean hasPermissionForHttpMethodByRoleNames(List<String> roleNames, String targetHttpMethod) {
        if (roleNames == null || roleNames.isEmpty() || targetHttpMethod == null || targetHttpMethod.trim().isEmpty()) {
            return false;
        }

        // Fetch roles by names and eagerly fetch permissions
        List<RoleEntity> roles = roleRepository.findByNameIn(roleNames);
        if (roles.isEmpty()) {
            return false; // No roles found for given names
        }
        return hasPermissionForHttpMethod(roles, targetHttpMethod); // Reuse the permission check method
    }
}
