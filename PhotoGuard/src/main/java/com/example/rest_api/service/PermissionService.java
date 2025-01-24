package com.example.rest_api.service;

import com.example.rest_api.database.usersdb.model.PermissionEntity;
import com.example.rest_api.database.usersdb.model.RolePermissionEntity;
import com.example.rest_api.database.usersdb.repository.PermissionRepository;
import com.example.rest_api.database.usersdb.repository.RolePermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
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
}
