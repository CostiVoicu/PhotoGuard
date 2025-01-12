package com.example.rest_api.service;

import com.example.rest_api.database.usersdb.model.*;
import com.example.rest_api.database.usersdb.repository.PermissionRepository;
import com.example.rest_api.database.usersdb.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public void save(RoleEntity role) {
        this.roleRepository.save(role);
    }

    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
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


}
