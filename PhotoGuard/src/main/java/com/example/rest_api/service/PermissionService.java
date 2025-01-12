package com.example.rest_api.service;

import com.example.rest_api.database.usersdb.model.PermissionEntity;
import com.example.rest_api.database.usersdb.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<PermissionEntity> findAllAlbumsPermissions() {
        return permissionRepository.findAllAlbumsPermissions();
    }

    public void save(PermissionEntity permissionEntity) {
        this.permissionRepository.save(permissionEntity);
    }

    public List<PermissionEntity> findAllById(List<Long> ids) {
        return permissionRepository.findAllById(ids);
    }
}
