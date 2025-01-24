package com.example.rest_api.service;

import com.example.rest_api.database.usersdb.model.*;
import com.example.rest_api.database.usersdb.repository.PermissionRepository;
import com.example.rest_api.database.usersdb.repository.RolePermissionRepository;
import com.example.rest_api.database.usersdb.repository.RoleRepository;
import com.example.rest_api.database.usersdb.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public void save(RoleEntity role) {
        this.roleRepository.save(role);
    }

    @Transactional
    public void deleteRoleById(Long roleId) {
        // Remove role from users first
        userRepository.removeUserRoleAssociations(roleId);

        // Delete role permissions
        rolePermissionRepository.deleteByRoleId(roleId);

        // Finally delete the role
        roleRepository.deleteById(roleId);
    }

    public Boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Optional<RoleEntity> findByName(String name) {
        return roleRepository.findByName(name);
    }
    public Optional<RoleEntity> findById(Long id) {
        return roleRepository.findById(id);
    }

    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    public void createAndAssignRolesToUser(Long userId, String albumName) {
        // Create the dynamic roles based on album name
        String albumRole = albumName.toUpperCase().replace(" ", "_") + "_ALBUM";
        String adminRole = albumName.toUpperCase().replace(" ", "_") + "_ALBUM_ADMIN";

        // Check if the roles exist, if not create them
        RoleEntity roleAlbum = roleRepository.findByName(albumRole).orElseGet(() -> {
            RoleEntity role = new RoleEntity();
            role.setName(albumRole);
            return roleRepository.save(role);
        });

        RoleEntity roleAlbumAdmin = roleRepository.findByName(adminRole).orElseGet(() -> {
            RoleEntity role = new RoleEntity();
            role.setName(adminRole);
            return roleRepository.save(role);
        });

        // Create permissions for the album
        List<PermissionEntity> permissions = createPermissionsForAlbum(albumName);

        // Save the permissions first
        permissions.forEach(permission -> permissionRepository.save(permission));

        // Assign only the first permission (GET) to roleAlbum if it's not already assigned
        PermissionEntity getPermission = permissions.get(0); // First permission (GET)
        if (!roleAlbum.getRolePermissions().stream()
                .anyMatch(rolePermission -> rolePermission.getPermission().equals(getPermission))) {
            RolePermissionEntity rolePermissionAlbum = new RolePermissionEntity();
            rolePermissionAlbum.setRole(roleAlbum);
            rolePermissionAlbum.setPermission(getPermission);
            roleAlbum.getRolePermissions().add(rolePermissionAlbum); // Add the permission to the role
        }

        // Assign all permissions to roleAlbumAdmin, but avoid duplicate assignments
        for (PermissionEntity permission : permissions) {
            if (!roleAlbumAdmin.getRolePermissions().stream()
                    .anyMatch(rolePermission -> rolePermission.getPermission().equals(permission))) {
                RolePermissionEntity rolePermissionAdmin = new RolePermissionEntity();
                rolePermissionAdmin.setRole(roleAlbumAdmin);
                rolePermissionAdmin.setPermission(permission);
                roleAlbumAdmin.getRolePermissions().add(rolePermissionAdmin); // Add each permission to the role
            }
        }

        // Save the roles with their permissions
        roleRepository.save(roleAlbum);
        roleRepository.save(roleAlbumAdmin);

        // Assign roles to the user
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<RoleEntity> roles = user.getRoles(); // Use List<RoleEntity> directly
        roles.add(roleAlbum);      // Add roleAlbum to user
        roles.add(roleAlbumAdmin); // Add roleAlbumAdmin to user

        user.setRoles(roles); // No need to convert List to Set
        userRepository.save(user); // Save the user with the assigned roles
    }

    public List<PermissionEntity> createPermissionsForAlbum(String albumName) {
        List<PermissionEntity> permissions = new ArrayList<>();

        // Define the URL path based on the album name
        String albumPath = "/album/" + albumName + "/**";

        // Create GET permission
        PermissionEntity getPermission = new PermissionEntity();
        getPermission.setHttpMethod("GET");
        getPermission.setUrl(albumPath);
        permissions.add(getPermission);

        // Create POST permission
        PermissionEntity postPermission = new PermissionEntity();
        postPermission.setHttpMethod("POST");
        postPermission.setUrl(albumPath);
        permissions.add(postPermission);

        // Create DELETE permission
        PermissionEntity deletePermission = new PermissionEntity();
        deletePermission.setHttpMethod("DELETE");
        deletePermission.setUrl(albumPath);
        permissions.add(deletePermission);

        return permissions;
    }
}
